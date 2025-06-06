package kr.hhplus.be.server.goods;

import kr.hhplus.be.server.goods.repository.GoodsDao;
import kr.hhplus.be.server.goods.service.DefaultGoodsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class SelectGoodsTest {

    @InjectMocks
    private DefaultGoodsService goodsService;

    @Mock
    private GoodsDao goodsDao;


    // 조회 시점의 상품별 잔여수량이 정확하도록.
    @DisplayName("상품조회 서비스를 호출한다.")
    @Test
    void callService() {
        // given
        final long goodsNo = 1L;

        // when
        goodsService.selectGoods(goodsNo);

        // then
        verify(goodsDao).getGoodsByGoodsNo(goodsNo);

    }
}
