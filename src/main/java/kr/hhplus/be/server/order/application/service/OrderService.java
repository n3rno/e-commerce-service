package kr.hhplus.be.server.order.application.service;

import kr.hhplus.be.server.goods.application.service.GoodsService;
import kr.hhplus.be.server.goods.domain.model.GoodsResponseDto;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderGoods;
import kr.hhplus.be.server.order.domain.model.OrderRequestDto;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.order.domain.service.OrderDomainService;
import kr.hhplus.be.server.order.domain.service.OrderIdGenerator;
import kr.hhplus.be.server.order.infrastructure.messaging.MessageProducer;
import kr.hhplus.be.server.point.domain.model.PointRequestDto;
import kr.hhplus.be.server.point.application.service.PointService;
import kr.hhplus.be.server.point.domain.model.enums.PointIdempotencyType;
import kr.hhplus.be.server.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    // 상품 여러종류 주문
    @Transactional
    public void order(OrderRequestDto orderRequestDto) {

        // 사용자 존재 여부 확인
        if (0 == userService.checkUserCountByUserNo(orderRequestDto.getUserNo())) {
            throw new IllegalArgumentException("Not Exist User");
        }

        OrderDomainService.ValidationResult validation = orderDomainService.validate(orderRequestDto);

        // 유효성 체크
        if (!validation.isValid()) {
            throw new IllegalArgumentException("Cannot order");
        }

        // 주문번호 생성
        String orderId = orderIdGenerator.generate();

        Order order = Order.builder()
                .id(orderId)
                .userNo(orderRequestDto.getUserNo())
                .couponIssueNo(null) // TODO 쿠폰 할인
                .totalOrderAmount(validation.getTotalPrice()).build();

        // 주문 이력 생성
        orderRepository.insertOrder(order);
        orderRepository.insertOrderGoods(OrderGoods.from(orderId, orderRequestDto.getOrderGoodsList()));

        // 포인트 차감
        pointService.use(PointRequestDto.builder()
                .userNo(orderRequestDto.getUserNo())
                .amount(validation.getTotalPrice())
                .orderId(orderId).build(), PointIdempotencyType.ORDER);


        // 주문 데이터 외부 전송
        messageProducer.send(order);
    }

    // 상품 1종 바로 주문하기
    @Transactional
    public void orderGoodsDirect(int goodsNo, int userNo) {
        final int quantity = 1;

        // 사용자 존재 여부 확인
        if (0 == userService.checkUserCountByUserNo(userNo)) {
            throw new IllegalArgumentException("Not Exist User");
        }

        // 상품 존재 확인
        GoodsResponseDto goods = goodsService.getGoodsByGoodsNo(goodsNo);
        if (null == goods) {
            throw new IllegalArgumentException("Not Exist Goods");
        }
        long totalAmount = quantity * goods.getPrice();

        // 주문번호 및 주문정보 생성
        String orderId = orderIdGenerator.generate();
        Order order = Order.builder()
                .id(orderId)
                .userNo(userNo)
                .couponIssueNo(null)
                .totalOrderAmount(totalAmount).build();

        try {
            // 상품 재고 차감
            goodsService.decreaseStock(goodsNo, quantity);

            // 포인트 차감
            pointService.use(PointRequestDto.builder()
                    .userNo(userNo)
                    .amount(totalAmount)
                    .orderId(orderId).build(), PointIdempotencyType.ORDER);

        } catch (IllegalAccessException | IllegalArgumentException e) {
            // 재고/잔액 부족 시 중단
            return;
        }

        // 주문 이력 생성
        orderRepository.insertOrder(order);
        orderRepository.insertOrderGoods(OrderGoods.from(orderId,
                List.of(new OrderRequestDto.OrderGoods(goodsNo, quantity))));
    }
}
