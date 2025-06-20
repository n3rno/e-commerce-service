## 문제 상황
다수 사용자가 동시에 같은 상품을 주문
→ 재고 oversell

## 해결 전략 
조건부 UPDATE 사용
```
UPDATE goods 
SET stock = stock - #{quantity}
WHERE no = #{goodsNo} 
  AND stock >= #{quantity};
```

## 테스트 결과