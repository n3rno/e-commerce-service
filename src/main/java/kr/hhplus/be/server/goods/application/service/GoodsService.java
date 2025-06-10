package kr.hhplus.be.server.goods.application.service;

import kr.hhplus.be.server.goods.domain.model.GoodsResponseDto;
import kr.hhplus.be.server.goods.domain.repository.GoodsRepository;
import kr.hhplus.be.server.order.domain.model.OrderRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GoodsService {

    private final GoodsRepository goodsRepository;

    public GoodsResponseDto getGoodsByGoodsNo(long goodsNo) {
        return goodsRepository.getGoods(goodsNo);
    }

    public List<GoodsResponseDto> getGoodsStockInfo(List<OrderRequestDto.OrderGoods> goodsList) {
        return goodsRepository.getGoodsStockInfo(goodsList);
    }
}
