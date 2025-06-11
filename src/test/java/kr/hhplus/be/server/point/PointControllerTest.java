package kr.hhplus.be.server.point;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.goods.infrastructure.persistence.mapper.GoodsMapper;
import kr.hhplus.be.server.order.infrastructure.persistence.mapper.OrderMapper;
import kr.hhplus.be.server.point.controller.PointController;
import kr.hhplus.be.server.point.domain.model.PointBalance;
import kr.hhplus.be.server.point.domain.model.PointChargeRequestDto;
import kr.hhplus.be.server.point.application.service.PointService;
import kr.hhplus.be.server.point.infrastructure.persistence.mapper.PointMapper;
import kr.hhplus.be.server.user.infrastructure.persistence.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointController.class)
public class PointControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PointService pointService;

    @MockitoBean
    private GoodsMapper goodsMapper;

    @MockitoBean
    private OrderMapper orderMapper;

    @MockitoBean
    private PointMapper pointMapper;

    @MockitoBean
    private UserMapper userMapper;

    @DisplayName("포인트 조회 api를 호출한다.")
    @Test
    void callControllerGetBalance() throws Exception {
        //given
        final int userNo = 1;
        final long balance = 0;
        final String requestParameter = "{\"userNo\": " + userNo + "}";
        PointBalance mockPoint = new PointBalance(balance, userNo);

        given(pointService.selectBalance(userNo)).willReturn(mockPoint);

        // when & then
        mockMvc.perform(post("/point/balance")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("userNo", String.valueOf(userNo)))
                .andExpect(status().isOk());

        // verify
        verify(pointService).selectBalance(userNo);
    }

    @DisplayName("충전 api를 호출한다.")
    @Test
    void callControllerCharge() throws Exception {
        //given
        final int userNo = 1;
        final long chargePoint = 1000;
        PointChargeRequestDto request = new PointChargeRequestDto(chargePoint, userNo);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(request);

        // when & then
        mockMvc.perform(post("/point/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        // verify
        verify(pointService).charge(refEq(request));
    }

}
