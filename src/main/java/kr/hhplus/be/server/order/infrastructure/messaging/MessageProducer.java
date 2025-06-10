package kr.hhplus.be.server.order.infrastructure.messaging;

import kr.hhplus.be.server.order.domain.model.Order;

public interface MessageProducer {
    void send(Order order);
}
