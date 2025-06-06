package kr.hhplus.be.server.goods.service;

import kr.hhplus.be.server.goods.model.GoodsResponseDto;
import kr.hhplus.be.server.goods.repository.GoodsDao;
import kr.hhplus.be.server.order.model.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DefaultGoodsService implements GoodsService {

    private final GoodsDao goodsDao;

    @Override
    public GoodsResponseDto selectGoods(long goodsNo) {
        return goodsDao.getGoodsByGoodsNo(goodsNo);
    }

    @Override
    public List<GoodsResponseDto> getGoodsStockInfo(List<OrderRequestDto.OrderGoods> goodsList) {
        return goodsDao.getGoodsByGoodsList(goodsList);
    }
}
