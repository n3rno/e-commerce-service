package kr.hhplus.be.server.goods.service;

import kr.hhplus.be.server.goods.model.GoodsResponseDto;
import kr.hhplus.be.server.order.model.OrderRequestDto;

import java.util.List;

public interface GoodsService {
    
    GoodsResponseDto selectGoods(long goodsNo);

    List<GoodsResponseDto> getGoodsStockInfo(List<OrderRequestDto.OrderGoods> goodsList);
}
