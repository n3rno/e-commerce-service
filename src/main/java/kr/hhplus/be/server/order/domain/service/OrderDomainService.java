package kr.hhplus.be.server.order.domain.service;

import kr.hhplus.be.server.goods.application.service.GoodsService;
import kr.hhplus.be.server.goods.domain.model.GoodsResponseDto;
import kr.hhplus.be.server.order.domain.model.OrderRequestDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


@Component
public class OrderDomainService {

    @Autowired
    private GoodsService goodsService;

    public ValidationResult validate(OrderRequestDto orderRequestDto) {

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

    @Getter
    @AllArgsConstructor
    public static class ValidationResult {
        private final boolean valid;
        private final long totalPrice;
    }
}
