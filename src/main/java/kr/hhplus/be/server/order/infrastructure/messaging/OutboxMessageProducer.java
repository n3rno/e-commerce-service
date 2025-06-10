package kr.hhplus.be.server.order.infrastructure.messaging;

import kr.hhplus.be.server.order.domain.model.Order;
import org.springframework.stereotype.Component;

@Component
public class OutboxMessageProducer implements MessageProducer {
    @Override
    public void send(Order order) {
        // TODO 실제 전송
        System.out.println("🧪 [MessageProducer] 주문 전송됨: " + order.getId());
    }
}
