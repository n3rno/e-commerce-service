package kr.hhplus.be.server.order.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class OutboxEvent {
    int seq;
    String type;
    String status;
    String payload;
}
