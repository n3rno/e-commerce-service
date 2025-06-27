## 문제 상황
동일 유저가 두 번 결제 요청하면
→ 잔액 음수 오류가 발생할 수 있다.

## 해결 전략 
point 차감(UPDATE) 시 `SELECT FOR UPDATE`으로 `비관적 락` 사용

## 테스트 결과
<pre> <code> <code>📁 <b>/src/test/java/kr/hhplus/be/server/order/RequestTwiceOrderTest.java</b></code> </code> </pre>

### 조건
- 5번 사용자 잔액  7,000원
- 5번 상품: 재고 50개, 가격 5,000원
- 5번 사용자가 5번 상품을 2회 주문 시도한다.

### 결과
- 최종 재고(goods.stock): 49
- order: 1 row 추가
- 잔액(point.point): 2,000 <br/>
  → 잔액 음수오류 발생 없이 정상 동작 확인 완료