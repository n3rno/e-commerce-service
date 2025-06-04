package kr.hhplus.be.server.goods.service;

import kr.hhplus.be.server.goods.model.GoodsResponseDto;

public interface GoodsService {
    
    GoodsResponseDto selectGoods(long goodsNo);

}
