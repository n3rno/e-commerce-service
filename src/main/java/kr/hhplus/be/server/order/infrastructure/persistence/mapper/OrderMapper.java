package kr.hhplus.be.server.order.infrastructure.persistence.mapper;

import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderGoods;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OrderMapper {
    void insertOrder(Order order);

    void insertOrderGoods(List<OrderGoods> orderGoodsList);
}
