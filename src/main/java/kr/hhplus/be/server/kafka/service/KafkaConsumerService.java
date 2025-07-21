package kr.hhplus.be.server.kafka.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.order.domain.model.OrderCompletedEvent;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // [컨슈머] 메시지 수신
    @KafkaListener(topics = "test-topic", groupId = "consumer-group-a")
    public void listen(ConsumerRecord<String, String> record) {
        System.out.println("Received: " + record.value());
    }

    @KafkaListener(topics = "order.completed", groupId = "order-group")
    public void listenOrder(String message) {
        try {
            OrderCompletedEvent orderCompletedEvent = objectMapper.readValue(message, OrderCompletedEvent.class);
            System.out.println("[Kafka] 수신된 주문 메시지: " + orderCompletedEvent.toString());

            // TODO 이미 처리된 이벤트인지 확인한다.
        } catch (JsonProcessingException e) {
            System.err.println("[kafka] 메시지 역질렬화 실패" + e.getMessage());
        }
    }
}
