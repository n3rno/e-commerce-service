## E-COMMERCE SERVICE 프로젝트


## [ERD](https://dbdiagram.io/d/e-commerce-68380c69c07db17e77ae5bb5)
<img src="./docs/erd/e-commerce.svg" alt="erd">

## Sequence Diagram
### Coupon
```mermaid
sequenceDiagram
    participant User
    participant Coupon
    participant Coupon Issue History

    User ->> Coupon: 쿠폰 발행 요청
    Coupon ->> Coupon: 남은 쿠폰 수 확인
    alt 남은 쿠폰이 없을 때
        Coupon -->> User: 쿠폰 발행 불가 응답
    else 남은 쿠폰이 있을 때
        Coupon ->> Coupon Issue History: 쿠폰 발행
        Coupon ->> Coupon : 남은 쿠폰 수 1 차감
        Coupon Issue History -->> User: 쿠폰 발행 완료 응답
    end
```

### Order
```mermaid
sequenceDiagram
    participant User
    participant Ordered Goods
    participant Point
    participant Order
    participant Data Analytics
    
    User ->> Ordered Goods: 상품 및 수량 선택
    
    alt Validation
        Ordered Goods ->> Ordered Goods: 재고 확인
        Ordered Goods -->> User: 재고가 없을 때, <br/>주문 불가
        Ordered Goods ->> Point: 잔액 확인 요청
        Point ->> Point: 잔액 확인
        Point -->> User: 잔액이 부족할 때, <br/>주문 불가
    end
    
        Point ->> Order: 주문 
        Order ->> Point: 포인트 차감
        Order ->> Data Analytics: 주문 데이터 외부 전송
        Order -->> User: 주문 성공 응답
```

## Infrastructure diagram
<img src="./docs/infra/infra-e-commerce.svg">
여러 클라이언트가 서버에 접속하여 상품을 주문할 수 있다. <br/>
주문 데이터는 database에 저장된다.
