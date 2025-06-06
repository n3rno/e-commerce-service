package kr.hhplus.be.server.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.order.model.OrderRequestDto;
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

import java.util.ArrayList;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @DisplayName("주문 api를 호출한다.")
    @Test
    void callController() throws Exception {
        //given
        final int userNo = 1;
        OrderGoods goods1 = OrderGoods.builder()
                .no(1)
                .quantity(5)
                .build();
        List<OrderGoods> orderGoodsList = new ArrayList<OrderGoods>();
        orderGoodsList.add(goods1);

        OrderRequestDto requestDto = new OrderRequestDto(userNo, goods1);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestDto);

        given(orderService.order(requestDto)).willReturn(null);

        // when & then
        mockMvc.perform(post("/order/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        // verify
        verify(orderService).order(requestDto);
    }


}
