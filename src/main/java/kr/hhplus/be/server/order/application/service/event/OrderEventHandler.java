package kr.hhplus.be.server.order.application.service.event;

import kr.hhplus.be.server.order.domain.model.OrderCompletedEvent;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderEventHandler {

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Retryable(
            value = RuntimeException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000) // 2초 간격 재시도
    )
    public void handleOrderCompleted(OrderCompletedEvent event) {
        // 외부 데이터 플랫폼으로 전송 (Mock API 호출)
        System.out.printf("[MockSend] 주문 완료 정보 전송: orderId=%s, userNo=%d, amount=%d%n",
                event.getOrderId(), event.getUserNo(), event.getTotalAmount());
        
        // 외부 api 호출 시 예외 던지도록 임의 설정
        throw new RuntimeException("Mock API 전송 실패");

        // 실제 구현 시 WebClient, RestTemplate 등으로 API 호출
    }
}
