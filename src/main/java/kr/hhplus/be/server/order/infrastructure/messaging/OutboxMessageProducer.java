package kr.hhplus.be.server.order.infrastructure.messaging;

import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OutboxEvent;
import kr.hhplus.be.server.order.infrastructure.persistence.mapper.OutboxEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
public class OutboxMessageProducer implements MessageProducer {

    private final OutboxEventMapper outboxEventMapper;

    @Override
    public int send(Order order) {

        String outboxEventOrder = "order_created";
        String status = "PENDING";

        OutboxEvent outboxEvent = OutboxEvent.builder().
                                    type(outboxEventOrder).
                                    payload(order.toString())
                                    .status(status).build();

        int result = outboxEventMapper.insertOutboxEvent(outboxEvent);

        System.out.println("[MessageProducer] 주문 전송됨: " + order.getId());
        return outboxEvent.getSeq();
    }

    @Override
    public void completed(int seq) {
        outboxEventMapper.updateOutboxEventStatus(
                OutboxEvent.builder()
                .seq(seq)
                .status("COMPLETED").build()
        );
        System.out.println("[MessageProducer] 전송 완료");
    }
}
