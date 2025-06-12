package kr.hhplus.be.server.order.infrastructure.messaging;

import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OutboxEvent;
import kr.hhplus.be.server.order.infrastructure.persistence.mapper.OutboxEventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

public class OutboxMessageProducer implements MessageProducer {

    @Autowired
    OutboxEventMapper outboxEventMapper;

    private static final String EVENT_TYPE_ORDER_CREATED = "order_created";
    private static final String STATUS_PENDING = "PENDING";
    private static final String STATUS_COMPLETED = "COMPLETED";
    private static final String STATUS_FAILED = "FAILED";

    @Override
    public void send(Order order) {

        OutboxEvent outboxEvent = OutboxEvent.builder().
                                    type(EVENT_TYPE_ORDER_CREATED).
                                    payload(order.toString())
                                    .status(STATUS_PENDING).build();

        try {
            outboxEventMapper.insertOutboxEvent(outboxEvent);
            System.out.println("[MessageProducer] 주문 전송됨: " + order.getId());

            completed(outboxEvent.getSeq());
        } catch (Exception e) {
            failed(outboxEvent.getSeq());
        }
    }

    
    // 성공 update 처리
    private void completed(int seq) {
        outboxEventMapper.updateOutboxEventStatus(
                OutboxEvent.builder()
                .seq(seq)
                .status(STATUS_COMPLETED).build()
        );
        System.out.println("[MessageProducer] 전송 완료 : " + seq);
    }

    // 실패 update 처리
    private void failed(int seq) {
        System.out.println("[MessageProducer] 전송 실패 : " + seq);

        outboxEventMapper.updateOutboxEventStatus(
                OutboxEvent.builder()
                .seq(seq)
                .status(STATUS_FAILED).build()
        );

    }
}
