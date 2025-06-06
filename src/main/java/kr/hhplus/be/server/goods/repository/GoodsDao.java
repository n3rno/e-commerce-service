package kr.hhplus.be.server.goods.repository;

import kr.hhplus.be.server.goods.model.GoodsResponseDto;
import kr.hhplus.be.server.order.model.OrderRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsDao {
    GoodsResponseDto getGoodsByGoodsNo(long goodsNo);

    List<GoodsResponseDto> getGoodsByGoodsList(List<OrderRequestDto.OrderGoods> goodsList);
}
