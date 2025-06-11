package kr.hhplus.be.server.order.infrastructure.messaging;

import kr.hhplus.be.server.order.domain.model.Order;
import org.springframework.stereotype.Component;

public class MockMessageProducer implements MessageProducer {
    @Override
    public int send(Order order) {
        // 실제 전송은 하지 않고 로그로 대체
        System.out.println("[FAKE] 주문 전송됨: " + order.getId());
        return 0;
    }

    @Override
    public void completed(int seq) {
        System.out.println("[FAKE] 주문 전송완료");
    }
}
