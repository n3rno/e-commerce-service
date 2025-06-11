package kr.hhplus.be.server.order.application.service;

import kr.hhplus.be.server.goods.application.service.GoodsService;
import kr.hhplus.be.server.goods.domain.model.GoodsResponseDto;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderGoods;
import kr.hhplus.be.server.order.domain.model.OrderRequestDto;
import kr.hhplus.be.server.order.infrastructure.messaging.MessageProducer;
import kr.hhplus.be.server.order.infrastructure.persistence.mapper.OrderMapper;
import kr.hhplus.be.server.order.infrastructure.messaging.MockMessageProducer;
import kr.hhplus.be.server.point.domain.model.PointChargeRequestDto;
import kr.hhplus.be.server.point.application.service.PointService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final PointService pointService;
    private final GoodsService goodsService;
    private final MockMessageProducer messageProducer;

    @Transactional
    public void order(OrderRequestDto orderRequestDto) {

        ValidationResult validation = validate(orderRequestDto);

        // 유효성 체크
        if (!validation.isValid()) {
            throw new IllegalArgumentException("Cannot order");
        }

        // 주문번호 생성
        String orderId = createOrderId();

        Order order = Order.builder()
                .id(orderId)
                .userNo(orderRequestDto.getUserNo())
                .couponIssueNo(null) // TODO 쿠폰 할인
                .totalOrderAmount(validation.getTotalPrice()).build();

        // 주문 이력 생성
        orderMapper.insertOrder(order);
        orderMapper.insertOrderGoods(OrderGoods.from(orderId, orderRequestDto.getOrderGoodsList()));

        // 포인트 차감
        pointService.use(PointChargeRequestDto.builder()
                .userNo(orderRequestDto.getUserNo())
                .amount(validation.getTotalPrice()).build());

        // 주문 데이터 외부 전송
        messageProducer.send(order);

    }

    private ValidationResult validate(OrderRequestDto orderRequestDto) {

        // 상품 정보 조회
        List<GoodsResponseDto> goodsStockList = goodsService.getGoodsStockInfo(orderRequestDto.getOrderGoodsList());

        // 사려는 수량이 현재 남아있는 재고보다 많은지 확인
        if (!checkStock(orderRequestDto.getOrderGoodsList(), goodsStockList)) {
            return new ValidationResult(false, 0);
        }

        // 결제 금액 계산
        long totalBuyPrice = calculateTotalPrice(orderRequestDto.getOrderGoodsList(), goodsStockList);

        return new ValidationResult(true, totalBuyPrice);
    }

    // 재고 수량 비교
    private boolean checkStock(List<OrderRequestDto.OrderGoods> goodsList, List<GoodsResponseDto> goodsStockList) {
        boolean isValid = true;

        for (GoodsResponseDto goods: goodsStockList) {
            Optional<OrderRequestDto.OrderGoods> goodsWillBuy = goodsList.stream()
                    .filter(item -> goods.getNo() == item.getGoodsNo()).findFirst();
            long buyStock = goodsWillBuy.get().getQuantity();

            // 사려는 수량이 현재 재고보다 많으면 중단
            if (goods.getRemainStock() < buyStock) {
                isValid = false;
                break;
            }
        }
        return isValid;
    }

    private long calculateTotalPrice(List<OrderRequestDto.OrderGoods> goodsList, List<GoodsResponseDto> goodsStockList) {
        long totalPrice = 0;

        for (GoodsResponseDto goods: goodsStockList) {
            Optional<OrderRequestDto.OrderGoods> goodsWillBuy = goodsList.stream()
                    .filter(item -> goods.getNo() == item.getGoodsNo()).findFirst();
            totalPrice += goodsWillBuy.get().getQuantity() * goods.getPrice();
        }

        return totalPrice;
    }

    private String createOrderId() {
        String now = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        return "E" + now;
    }

    @Getter
    @AllArgsConstructor
    public static class ValidationResult {
        private final boolean valid;
        private final long totalPrice;
    }

}
