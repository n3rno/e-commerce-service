package kr.hhplus.be.server.order.application.service;

import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderGoods;
import kr.hhplus.be.server.order.domain.model.OrderRequestDto;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.order.domain.service.OrderDomainService;
import kr.hhplus.be.server.order.domain.service.OrderIdGenerator;
import kr.hhplus.be.server.order.infrastructure.messaging.MessageProducer;
import kr.hhplus.be.server.point.domain.model.PointChargeRequestDto;
import kr.hhplus.be.server.point.application.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PointService pointService;
    private final MessageProducer messageProducer;
    private final OrderIdGenerator orderIdGenerator;
    private final OrderDomainService orderDomainService;

    @Transactional
    public void order(OrderRequestDto orderRequestDto) {

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
        pointService.use(PointChargeRequestDto.builder()
                .userNo(orderRequestDto.getUserNo())
                .amount(validation.getTotalPrice()).build());

        // 주문 데이터 외부 전송
        messageProducer.send(order);
    }
}
