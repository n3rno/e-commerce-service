package kr.hhplus.be.server.order.domain.repository;

import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderGoods;

import java.util.List;

public interface OrderRepository {

    void insertOrder(Order order);
    void insertOrderGoods(List<OrderGoods> orderGoodsList);
}
