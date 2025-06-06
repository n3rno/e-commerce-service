package kr.hhplus.be.server.order.service;

import kr.hhplus.be.server.order.model.Order;

public interface OrderEventDataSender {
    void send(Order order);
}
