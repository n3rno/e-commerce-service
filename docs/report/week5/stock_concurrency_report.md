## 문제 상황
다수 사용자가 동시에 같은 상품을 주문하면
→ 재고 oversell 될 수 있다.

## 해결 전략 
상품 재고 차감 시 `조건부 UPDATE` 사용
```
UPDATE goods
SET stock = stock - #{quantity}
WHERE no = #{goodsNo}
  AND stock >= #{quantity}
```

## 테스트 결과
<pre> <code> <code>📁 <b>/src/test/java/kr/hhplus/be/server/order/StockOversellTest.java</b></code> </code> </pre>

### 조건
- 6번 사용자 잔액: 300,000원
- 6번 상품: 재고 100개 / 가격 2,000원
- 6번 사용자가 6번 상품을 100회 주문 시도한다.

### 결과
- 최종 재고(goods.stock): 0
- order: 100 row 추가
- 잔액(point.point): 100,000 <br/>
→ 정상 동작 확인 완료
