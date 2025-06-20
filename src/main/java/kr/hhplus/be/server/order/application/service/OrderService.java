package kr.hhplus.be.server.order.application.service;

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
}
