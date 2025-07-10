package kr.hhplus.be.server.order.domain.model;

import jakarta.validation.constraints.Positive;
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
        int goodsNo;
        @Positive(message = "구매수량은 1보다 커야 합니다.")
        int quantity;
    }
}
