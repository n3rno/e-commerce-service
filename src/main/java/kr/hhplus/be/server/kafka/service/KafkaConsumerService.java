package kr.hhplus.be.server.kafka.service;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    // [컨슈머] 메시지 수신
    @KafkaListener(topics = "test-topic", groupId = "my-consumer-group")
    public void listen(ConsumerRecord<String, String> record) {
        System.out.println("Received: " + record.value());
    }
}
