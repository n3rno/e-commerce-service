package kr.hhplus.be.server.order;

import kr.hhplus.be.server.goods.application.service.GoodsService;
import kr.hhplus.be.server.goods.domain.model.GoodsResponseDto;
import kr.hhplus.be.server.order.domain.model.Order;
import kr.hhplus.be.server.order.domain.model.OrderGoods;
import kr.hhplus.be.server.order.domain.model.OrderRequestDto;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.order.infrastructure.messaging.MessageProducer;
import kr.hhplus.be.server.point.domain.model.PointChargeRequestDto;
import kr.hhplus.be.server.point.application.service.PointService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PointService pointService;

    @MockitoBean
    private MessageProducer mockMessageProducer;

    @DisplayName("존재하지 않는 사용자는 Exception을 발생시킨다.")
    @Test
    void ifNullThenReturnZeroBalance() {
        //given
        final int userNo = 0;

        //when then
        assertThatThrownBy(() -> pointService.selectBalance(userNo));
    }

    @DisplayName("상품 금액과 총 재고, 남은 재고를 조회한다.")
    @Test
    void selectOrderGoodsInfo() {
        //given
        OrderRequestDto.OrderGoods goods1 = new OrderRequestDto.OrderGoods(1, 5);
        List<OrderRequestDto.OrderGoods> orderGoodsList = new ArrayList<OrderRequestDto.OrderGoods>();
        orderGoodsList.add(goods1);

        // when
        List<GoodsResponseDto> goodsList = goodsService.getGoodsStockInfo(orderGoodsList);

        // then
        assertThat(goodsList.size()).isEqualTo(1);
        assertThat(goodsList.get(0).getStock()).isEqualTo(30);
        assertThat(goodsList.get(0).getRemainStock()).isEqualTo(30);
    }

    @DisplayName("주문 이력을 생성한다.")
    @Test
    @Disabled
    void addOrderHistory() {
        // given
        OrderRequestDto.OrderGoods goods1 = new OrderRequestDto.OrderGoods(3, 5);
        List<OrderRequestDto.OrderGoods> orderGoodsList = new ArrayList<OrderRequestDto.OrderGoods>();
        orderGoodsList.add(goods1);
        String orderId = "E202506062020000";

        // when
        Order order = Order.builder()
                .id(orderId)
                .userNo(1)
                .couponIssueNo(null)
                .totalOrderAmount(1500).build();

        // then
        assertThatCode(()-> {
            // 주문 이력 생성
            orderRepository.insertOrder(order);
            orderRepository.insertOrderGoods(OrderGoods.from(orderId, orderGoodsList));
        }).doesNotThrowAnyException();
    }

    @DisplayName("잔액을 차감한다(결제).")
    @Test
    void usePoint() {
        //given
        PointChargeRequestDto pointUseDto = PointChargeRequestDto.builder()
                        .userNo(1)
                        .amount(0)
                        .build();
        // when
        // then
        assertThatCode(() -> pointService.use(pointUseDto)).doesNotThrowAnyException();
    }

    @DisplayName("잔액이 부족한 경우 차감할 수 없다.")
    @Test
    void cannotUsePoint() {
        //given
        PointChargeRequestDto pointUseDto = PointChargeRequestDto.builder()
                .userNo(1)
                .amount(100000)
                .build();
        // when
        // then
        assertThatCode(() -> pointService.use(pointUseDto)).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 정보를 데이터플랫폼에 전송한다.")
    @Test
    void sendOrderInfoToDataAnalytics() {
        // given
        Order order = Order.builder()
                .id("E202506062020000")
                .userNo(1)
                .couponIssueNo(null)
                .totalOrderAmount(1500).build();

        // when then
        int seq = mockMessageProducer.send(order);
        mockMessageProducer.completed(seq);
    }

}
