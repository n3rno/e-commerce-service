package kr.hhplus.be.server.order.model;
s
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class OrderRequestDto {
    int userNo;
    List<OrderGoods> orderGoodsList;

    @Getter
    public static class OrderGoods {
        long goodsNo;
        long quantity;
    }
}
