package kr.hhplus.be.server.order.repository;

import kr.hhplus.be.server.order.model.Order;
import kr.hhplus.be.server.order.model.OrderGoods;
import kr.hhplus.be.server.order.model.OrderRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface OrderDao {
    void insertOrder(Order order);

    void insertOrderGoods(List<OrderGoods> orderGoodsList);
}
