package kr.hhplus.be.server.point;

import kr.hhplus.be.server.point.controller.PointController;
import kr.hhplus.be.server.point.model.PointBalance;
import kr.hhplus.be.server.point.model.PointChargeRequestDto;
import kr.hhplus.be.server.point.service.PointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

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

    @DisplayName("포인트 조회 api를 호출한다.")
    @Test
    void callControllerGetBalance() throws Exception {
        //given
        final int userNo = 1;
        final long balance = 1000L;
        final String requestParameter = "{\"userNo\": " + userNo + "}";
        PointBalance mockPoint = new PointBalance(balance, userNo);

        given(pointService.selectBalance(userNo)).willReturn(mockPoint);

        // when & then
        mockMvc.perform(post("/point/balance")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestParameter))
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
        final String requestParameter = "{\"userNo\": " + userNo + ", \"chargePoint\": 1000}";
        PointChargeRequestDto request = new PointChargeRequestDto(chargePoint, userNo);

        // when & then
        mockMvc.perform(post("/point/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestParameter))
                .andExpect(status().isOk());

        // verify
        verify(pointService).charge(request);
    }

}
