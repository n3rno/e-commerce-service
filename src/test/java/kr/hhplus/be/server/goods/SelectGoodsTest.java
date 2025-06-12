package kr.hhplus.be.server.goods;

import kr.hhplus.be.server.goods.domain.repository.GoodsRepository;
import kr.hhplus.be.server.goods.infrastructure.persistence.mapper.GoodsMapper;
import kr.hhplus.be.server.goods.application.service.GoodsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SelectGoodsTest {

    @InjectMocks
    private GoodsService goodsService;

    @Mock
    private GoodsRepository goodsRepository;

    @Mock
    private GoodsMapper goodsMapper;


    // 조회 시점의 상품별 잔여수량이 정확하도록.
    @DisplayName("상품조회 서비스를 호출한다.")
    @Test
    void callService() {
        // given
        final long goodsNo = 1L;

        // when
        goodsService.getGoodsByGoodsNo(goodsNo);

        // then
        verify(goodsRepository).getGoodsByGoodsNo(goodsNo);

    }
}
