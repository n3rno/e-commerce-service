package kr.hhplus.be.server.order.application.service;

import kr.hhplus.be.server.order.domain.model.OrderCompletedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderEventHandler {

    @EventListener
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleOrderCompleted(OrderCompletedEvent event) {
        // 외부 데이터 플랫폼으로 전송 (Mock API 호출)
        System.out.printf("[MockSend] 주문 완료 정보 전송: orderId=%s, userNo=%d, amount=%d%n",
                event.getOrderId(), event.getUserNo(), event.getTotalAmount());

        // 실제 구현 시 WebClient, RestTemplate 등으로 API 호출
    }
}
