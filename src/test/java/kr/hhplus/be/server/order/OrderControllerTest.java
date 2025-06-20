package kr.hhplus.be.server.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.goods.infrastructure.persistence.mapper.GoodsMapper;
import kr.hhplus.be.server.order.controller.OrderController;
import kr.hhplus.be.server.order.domain.model.OrderRequestDto;
import kr.hhplus.be.server.order.application.service.OrderService;
import kr.hhplus.be.server.order.infrastructure.persistence.mapper.OrderMapper;
import kr.hhplus.be.server.point.infrastructure.persistence.mapper.PointMapper;
import kr.hhplus.be.server.user.infrastructure.persistence.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OrderService orderService;

    @MockitoBean
    private GoodsMapper goodsMapper;

    @MockitoBean
    private OrderMapper orderMapper;

    @MockitoBean
    private PointMapper pointMapper;

    @MockitoBean
    private UserMapper userMapper;

    @DisplayName("주문 api를 호출한다.")
    @Test
    void callController() throws Exception {
        //given
        final int userNo = 1;
        OrderRequestDto.OrderGoods goods1 = new OrderRequestDto.OrderGoods(1, 5);
        List<OrderRequestDto.OrderGoods> orderGoodsList = new ArrayList<OrderRequestDto.OrderGoods>();
        orderGoodsList.add(goods1);

        OrderRequestDto requestDto = new OrderRequestDto(userNo, orderGoodsList);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(requestDto);

        // when & then
        mockMvc.perform(post("/order/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk());

        // verify using ArgumentCaptor
        ArgumentCaptor<OrderRequestDto> captor = ArgumentCaptor.forClass(OrderRequestDto.class);
        verify(orderService).order(captor.capture());

        OrderRequestDto captured = captor.getValue();
        assert captured.getUserNo() == userNo;
        assert captured.getOrderGoodsList().get(0).getGoodsNo() == goods1.getGoodsNo();
        assert captured.getOrderGoodsList().get(0).getQuantity() == goods1.getQuantity();
    }
}
