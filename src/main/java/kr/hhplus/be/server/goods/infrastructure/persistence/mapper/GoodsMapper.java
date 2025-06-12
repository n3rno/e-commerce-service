package kr.hhplus.be.server.goods.infrastructure.persistence.mapper;

import kr.hhplus.be.server.goods.domain.model.GoodsResponseDto;
import kr.hhplus.be.server.order.domain.model.OrderRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface GoodsMapper {
    GoodsResponseDto selectGoodsByGoodsNo(long goodsNo);

    List<GoodsResponseDto> selectGoodsByGoodsList(List<OrderRequestDto.OrderGoods> goodsList);
}
