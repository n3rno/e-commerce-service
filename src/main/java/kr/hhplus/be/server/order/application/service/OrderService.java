package kr.hhplus.be.server.order.application.service;

import kr.hhplus.be.server.Exception.EcommerceException;
import kr.hhplus.be.server.Exception.ErrorCode;
import kr.hhplus.be.server.Exception.OutOfStockException;
import kr.hhplus.be.server.goods.application.service.GoodsService;
import kr.hhplus.be.server.goods.domain.model.GoodsResponseDto;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderCompletedEvent;
import kr.hhplus.be.server.order.domain.model.OrderGoods;
import kr.hhplus.be.server.order.domain.model.OrderRequestDto;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.order.domain.service.OrderDomainService;
import kr.hhplus.be.server.order.domain.service.OrderIdGenerator;
import kr.hhplus.be.server.order.infrastructure.messaging.MessageProducer;
import kr.hhplus.be.server.point.domain.model.PointRequestDto;
import kr.hhplus.be.server.point.application.service.PointService;
import kr.hhplus.be.server.point.domain.model.enums.PointIdempotencyType;
import kr.hhplus.be.server.ranking.service.GoodsRankingService;
import kr.hhplus.be.server.redis.RedisLockManager;
import kr.hhplus.be.server.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PointService pointService;
    private final MessageProducer messageProducer;
    private final OrderIdGenerator orderIdGenerator;
    private final OrderDomainService orderDomainService;
    private final UserService userService;
    private final GoodsService goodsService;
    private final RedisLockManager redisLockManager;
    private final GoodsRankingService goodsRankingService;
    private final ApplicationEventPublisher applicationEventPublisher;

    // 상품 여러종류 주문
    @Transactional
    public void order(OrderRequestDto orderRequestDto) {

        // 사용자 존재 여부 확인
        if (0 == userService.checkUserCountByUserNo(orderRequestDto.getUserNo())) {
            throw new EcommerceException(ErrorCode.NOT_EXIST_USER);
        }

        OrderDomainService.ValidationResult validation = orderDomainService.validate(orderRequestDto);

        // 유효성 체크
        if (!validation.isValid()) {
            throw new EcommerceException(ErrorCode.IMPOSSIBLE_ORDER);
        }

        // 주문번호 생성
        String orderId = orderIdGenerator.generate();

        Order order = Order.builder()
                .id(orderId)
                .userNo(orderRequestDto.getUserNo())
                .couponIssueNo(null) // TODO 쿠폰 할인
                .totalOrderAmount(validation.getTotalPrice()).build();

        String lockKey = "lock:order:user:" + orderRequestDto.getUserNo();
        String lockValue = UUID.randomUUID().toString();

        boolean locked = redisLockManager.tryLock(lockKey, lockValue, Duration.ofSeconds(5));
        if (!locked) {
            // 다른 요청 처리 중
            throw new EcommerceException(ErrorCode.REQUEST_LOCKED);
        }

        try {
            // 락이 보장된다

            // 포인트 차감
            pointService.use(PointRequestDto.builder()
                    .userNo(orderRequestDto.getUserNo())
                    .amount(validation.getTotalPrice())
                    .orderId(orderId).build(), PointIdempotencyType.ORDER);

            orderRequestDto.getOrderGoodsList().forEach(goods -> {
                // 상품 재고 차감
                goodsService.decreaseStock(goods.getGoodsNo(), goods.getQuantity());

            });

        } catch (EcommerceException e) {
            // 재고 부족 시 중단
            throw new EcommerceException(ErrorCode.LACK_OF_STOCK);
        } finally {
            redisLockManager.releaseLock(lockKey, lockValue);
        }

            // 주문 이력 생성
            orderRepository.insertOrder(order);
            orderRepository.insertOrderGoods(OrderGoods.from(orderId, orderRequestDto.getOrderGoodsList()));

        // 주문 데이터 외부 전송
//        messageProducer.send(order);
        applicationEventPublisher.publishEvent(new OrderCompletedEvent(orderId, orderRequestDto.getUserNo(), validation.getTotalPrice()));
    }

    // 상품 1종 바로 주문하기
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void orderGoodsDirect(int goodsNo, int userNo) {
        final int quantity = 1;

        // 사용자 존재 여부 확인
        if (0 == userService.checkUserCountByUserNo(userNo)) {
            throw new EcommerceException(ErrorCode.NOT_EXIST_USER);
        }

        // 상품 존재 확인
        // TODO findByGoodsNoForUpdate 만들기
        GoodsResponseDto goods = goodsService.getGoodsByGoodsNo(goodsNo);
        if (null == goods) {
            throw new EcommerceException(ErrorCode.NOT_EXIST_GOODS);
        }
        long totalAmount = quantity * goods.getPrice();

        // 주문번호 및 주문정보 생성
        String orderId = orderIdGenerator.generate();
        Order order = Order.builder()
                .id(orderId)
                .userNo(userNo)
                .totalOrderAmount(totalAmount).build();

        String lockKey = "lock:order:user:" + userNo;
        String lockValue = UUID.randomUUID().toString();

        boolean locked = redisLockManager.tryLock(lockKey, lockValue, Duration.ofSeconds(5));
        if (!locked) {
            // 다른 요청 처리 중
            throw new EcommerceException(ErrorCode.REQUEST_LOCKED);
        }

        try {
            // 락이 보장된다.

            // 포인트 차감
            pointService.use(PointRequestDto.builder()
                    .userNo(userNo)
                    .amount(totalAmount)
                    .orderId(orderId).build(), PointIdempotencyType.ORDER);

            // 상품 재고 차감
            goodsService.decreaseStock(goodsNo, quantity);
        } catch (EcommerceException e) {
            // 재고 부족 시 중단
            throw new EcommerceException(ErrorCode.LACK_OF_STOCK);
        } finally {
            redisLockManager.releaseLock(lockKey, lockValue);
        }

        // 주문 이력 생성
        orderRepository.insertOrder(order);
        orderRepository.insertOrderGoods(OrderGoods.from(orderId,
                List.of(new OrderRequestDto.OrderGoods(goodsNo, quantity))));

        // 상품 주문 랭킹 기록
        goodsRankingService.increaseGoodsScore(goodsNo, quantity);

        applicationEventPublisher.publishEvent(new OrderCompletedEvent(orderId, userNo, totalAmount));
    }
}
