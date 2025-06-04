package kr.hhplus.be.server.goods.service;

import kr.hhplus.be.server.goods.model.GoodsResponseDto;
import kr.hhplus.be.server.goods.repository.GoodsDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DefaultGoodsService implements GoodsService {

    private final GoodsDao goodsDao;

    @Override
    public GoodsResponseDto selectGoods(long goodsNo) {
        return goodsDao.getGoodsByGoodsNo(goodsNo);
    }
}
