package kr.hhplus.be.server.order;

import kr.hhplus.be.server.order.repository.OrderDao;
import kr.hhplus.be.server.order.service.OrderService;
import kr.hhplus.be.server.point.model.PointBalance;
import kr.hhplus.be.server.point.repository.PointDao;
import kr.hhplus.be.server.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    // TODO 맞는지 확인
    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private UserService userService;

    @DisplayName("유효한 사용자인지 확인한다.")
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
        OrderGoods goods1 = OrderGoods.builder()
                .no(1)
                .quantity(5)
                .build();
        List<OrderGoods> orderGoodsList = new ArrayList<OrderGoods>();
        orderGoodsList.add(goods1);

        // when
        List<Goods> goodsList = goodsService.selectGoodsInfoList(goods1);

        // then
        assertThat(goodsList.size()).isEqualTo(0);
    }

    @DisplayName("주문 이력을 생성한다.")
    void addOrderHistory() {
        // given
        OrderGoods goods1 = OrderGoods.builder()
                .no(1)
                .quantity(5)
                .build();
        List<OrderGoods> orderGoodsList = new ArrayList<OrderGoods>();
        orderGoodsList.add(goods1);

        // when
        // then
        assertThatCode(orderMapper.insertOrder(orderGoodsList)).doesNotThrowAnyException();

    }

    @DisplayName("잔액을 차감한다(결제).")
    void usdPoint() {
        //given
        PointUseDto pointUseDto = PointUseDto.builder()
                        .userNo(1)
                        .amount(1000)
                        .build();
        // when
        // then
        assertThatCode(pointService.use(pointUseDto)).doesNotThrowAnyException();
    }

    @DisplayName("주문 정보를 데이터플랫폼에 전송한다.")
    void sendOrderInfoToDataAnalytics() {
        // ?????
    }




}
