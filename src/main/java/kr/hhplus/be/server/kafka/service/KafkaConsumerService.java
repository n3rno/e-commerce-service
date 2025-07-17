package kr.hhplus.be.server.kafka.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    // [컨슈머] 메시지 수신
    @KafkaListener(topics = "test-topic", groupId = "consumer-group-a")
    public void listen(ConsumerRecord<String, String> record) {
        System.out.println("Received: " + record.value());
    }

    @KafkaListener(topics = "order.completed", groupId = "order-group")
    public void listenOrder(String message) {
        System.out.println("[Kafka] 수신된 주문 메시지: " + message);
    }
}
