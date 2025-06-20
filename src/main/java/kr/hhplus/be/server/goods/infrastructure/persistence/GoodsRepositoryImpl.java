package kr.hhplus.be.server.goods.infrastructure.persistence;

import kr.hhplus.be.server.goods.domain.model.GoodsResponseDto;
import kr.hhplus.be.server.goods.domain.repository.GoodsRepository;
import kr.hhplus.be.server.goods.infrastructure.persistence.mapper.GoodsMapper;
import kr.hhplus.be.server.order.domain.model.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class GoodsRepositoryImpl implements GoodsRepository {
    private final GoodsMapper goodsMapper;

    @Override
    public GoodsResponseDto getGoodsByGoodsNo(long goodsNo) {
        return goodsMapper.selectGoodsByGoodsNo(goodsNo);
    }

    @Override
    public List<GoodsResponseDto> getGoodsStockInfo(List<OrderRequestDto.OrderGoods> goodsList) {
        return goodsMapper.selectGoodsByGoodsList(goodsList);
    }
}
