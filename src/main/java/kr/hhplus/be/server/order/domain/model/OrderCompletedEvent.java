package kr.hhplus.be.server.order.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OrderCompletedEvent {
    private final String orderId;
    private final int userNo;
    private final long totalAmount;
}
