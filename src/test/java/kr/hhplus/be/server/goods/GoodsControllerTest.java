package kr.hhplus.be.server.goods;

import kr.hhplus.be.server.goods.controller.GoodsController;
import kr.hhplus.be.server.goods.domain.model.GoodsResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GoodsController.class)
public class GoodsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GoodsService goodsService;

    @DisplayName("상품조회 api를 호출한다.")
    @Test
    void callController() throws Exception {
        //given
        final long goodsNo = 1L;
        GoodsResponseDto mockGoods = new GoodsResponseDto(goodsNo, "커피", 3000, 50, 50);

        given(goodsService.selectGoods(anyLong())).willReturn(mockGoods);

        // when & then
        mockMvc.perform(get("/goods/get/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.no").value(goodsNo));

        // verify
        verify(goodsService).selectGoods(goodsNo);
    }

}
