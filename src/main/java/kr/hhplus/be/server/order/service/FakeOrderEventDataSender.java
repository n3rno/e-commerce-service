package kr.hhplus.be.server.order.service;

import kr.hhplus.be.server.order.model.Order;
import org.springframework.stereotype.Component;

@Component
public class FakeOrderEventDataSender implements OrderEventDataSender {
    @Override
    public void send(Order order) {
        // 실제 전송은 하지 않고 로그로 대체
        System.out.println("🧪 [FAKE] 주문 전송됨: " + order.getId());
    }
}
