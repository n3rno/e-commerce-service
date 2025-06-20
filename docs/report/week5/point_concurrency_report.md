## 문제 상황
동일 유저가 두 번 결제 요청하면
→ 잔액 음수 오류가 발생할 수 있다.

## 해결 전략 
point 차감 시 `SELECT FOR UPDATE`으로 `비관적 락` 사용

## 테스트 결과
<pre> <code> <code>📁 <b>/src/test/java/kr/hhplus/be/server/order/RequestTwiceOrderTest.java</b></code> </code> </pre>