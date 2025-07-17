package kr.hhplus.be.server.Exception;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(String goodsName, long buyStock) {
        super(String.format("상품 '%s'의 재고가 부족합니다. 요청: %d개", goodsName, buyStock));
    }
}
