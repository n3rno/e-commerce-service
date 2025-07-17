package kr.hhplus.be.server.kafka.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class KafkaProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;

    // [프로듀서] 메시지 전송
    public void send(String topic, String message) {
        kafkaTemplate.send(topic, message);
    }
}
