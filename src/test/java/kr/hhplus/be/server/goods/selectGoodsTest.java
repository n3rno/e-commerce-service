package kr.hhplus.be.server.goods;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class selectGoodsTest {

    @Autowired
    private MockMvc mockMvc;

    private final GoodsService goodsService;
    private final GoodsDao goodsDao;

    @DisplayName("상품조회 api를 호출한다.")
    @Test
    void callController() {
        //given
        final long goodsNo = 1L;
        GoodsDto mockGoods = new GoodsDto(goodsNo);

        given(goodsService.selectGoods(goodsNo)).willReturn(mockGoods);

        // when & then
        mockMvc.perform(get("/goods/{no}"), goodsNo)
                .accept(MediaType.APPLICATION_JSON)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(goodsNo))
                .andExpect(jsonPath("$.name").value(""))
                .andExpect(jsonPath("$.price").value(""));

        // verify
        verify(goodsService).getGoodsByGoodsNo(goodsNo);
    }


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
