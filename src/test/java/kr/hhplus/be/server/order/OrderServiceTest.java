package kr.hhplus.be.server.order;

import kr.hhplus.be.server.goods.model.GoodsResponseDto;
import kr.hhplus.be.server.goods.service.GoodsService;
import kr.hhplus.be.server.order.model.Order;
import kr.hhplus.be.server.order.model.OrderGoods;
import kr.hhplus.be.server.order.model.OrderRequestDto;
import kr.hhplus.be.server.order.repository.OrderDao;
import kr.hhplus.be.server.order.service.FakeOrderEventDataSender;
import kr.hhplus.be.server.point.model.PointBalance;
import kr.hhplus.be.server.point.model.PointChargeRequestDto;
import kr.hhplus.be.server.point.service.PointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderDao orderDao;

    @Autowired
    private PointService pointService;

    @Mock
    private FakeOrderEventDataSender fakeOrderEventDataSender;

    @DisplayName("존재하지 않는 사용자는 잔액을 0원 반환한다.")
    @Test
    void ifNullThenReturnZeroBalance() {
        //given
        final int userNo = 0;

        //when
        PointBalance balance = pointService.selectBalance(userNo);

        //then
        assertThat(balance.getBalance()).isEqualTo(0);
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
    void addOrderHistory() {
        // given
        OrderRequestDto.OrderGoods goods1 = new OrderRequestDto.OrderGoods(1, 5);
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
            orderDao.insertOrder(order);
            orderDao.insertOrderGoods(OrderGoods.from(orderId, orderGoodsList));
        }).doesNotThrowAnyException();
    }

    @DisplayName("잔액을 차감한다(결제).")
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
    void sendOrderInfoToDataAnalytics() {
        // given
        Order order = Order.builder()
                .id("E202506062020000")
                .userNo(1)
                .couponIssueNo(null)
                .totalOrderAmount(1500).build();

        // when then
        fakeOrderEventDataSender.send(order);
    }




}
