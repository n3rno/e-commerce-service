package kr.hhplus.be.server.kafka.controller;

import kr.hhplus.be.server.kafka.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kafka")
@RequiredArgsConstructor
public class KafkaTestController {

    private final KafkaProducerService producer;

    @PostMapping("/send")
    public void send(@RequestParam String message) {
        producer.send("test-topic", message);
    }
}
