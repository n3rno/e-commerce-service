package kr.hhplus.be.server.goods.repository;

import kr.hhplus.be.server.goods.model.GoodsResponseDto;

public interface GoodsDao {
    GoodsResponseDto getGoodsByGoodsNo(long goodsNo);
}
