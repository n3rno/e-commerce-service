package kr.hhplus.be.server.goods.domain.repository;

import kr.hhplus.be.server.goods.domain.model.GoodsResponseDto;
import kr.hhplus.be.server.order.domain.model.OrderRequestDto;

import java.util.List;

public interface GoodsRepository {

    GoodsResponseDto getGoodsByGoodsNo(long goodsNo);

    List<GoodsResponseDto> getGoodsStockInfo(List<OrderRequestDto.OrderGoods> goodsList);
}
