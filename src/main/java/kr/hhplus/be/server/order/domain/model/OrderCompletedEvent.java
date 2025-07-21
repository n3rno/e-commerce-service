package kr.hhplus.be.server.order.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor(force = true)
@AllArgsConstructor
@Getter
@Setter
public class OrderCompletedEvent {
    private final String orderId;
    private final int userNo;
    private final long totalAmount;
}
