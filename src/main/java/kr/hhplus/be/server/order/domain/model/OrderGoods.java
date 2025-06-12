package kr.hhplus.be.server.order.domain.model;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
//@Builder
public class OrderGoods {
    int orderGoodsNo;
    String orderId;
    long goodsNo;
    long quantity;
    LocalDateTime createdAt;

    // 생성자
    public OrderGoods(String orderId, long goodsNo, long quantity) {
        this.orderId = orderId;
        this.goodsNo = goodsNo;
        this.quantity = quantity;
    }

    // ✅ List<OrderGoods> 생성하는 정적 메서드
    public static List<OrderGoods> from(String orderId, List<OrderRequestDto.OrderGoods> goodsList) {
        return goodsList.stream()
                .map(g -> new OrderGoods(orderId, g.getGoodsNo(), g.getQuantity()))
                .toList();
    }
}
