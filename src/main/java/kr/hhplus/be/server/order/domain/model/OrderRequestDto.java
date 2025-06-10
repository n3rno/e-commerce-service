package kr.hhplus.be.server.order.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OrderRequestDto {
    int userNo;
    List<OrderGoods> orderGoodsList;

    @Getter
    @AllArgsConstructor
    public static class OrderGoods {
        long goodsNo;
        long quantity;
    }
}
