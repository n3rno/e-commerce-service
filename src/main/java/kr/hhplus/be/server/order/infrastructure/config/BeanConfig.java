package kr.hhplus.be.server.order.infrastructure.config;

import kr.hhplus.be.server.order.infrastructure.messaging.MockMessageProducer;
import kr.hhplus.be.server.order.infrastructure.messaging.MessageProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

// 테스트 가능하도록 의존 주입
@Configuration
public class BeanConfig {
    @Bean
    @Profile("test")
    public MessageProducer testMessageProducer() {
        return new MockMessageProducer();
    }

    // TODO
//    @Bean
//    @Profile("prod")
//    public MessageProducer prodMessageProducer(OutboxRepository outboxRepository) {
//        return new OutboxMessageProducer(outboxRepository);
//    }
}
