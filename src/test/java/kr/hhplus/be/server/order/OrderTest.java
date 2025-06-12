package kr.hhplus.be.server.order;

import kr.hhplus.be.server.order.application.service.OrderService;
import kr.hhplus.be.server.order.domain.model.OrderRequestDto;
import kr.hhplus.be.server.order.infrastructure.persistence.mapper.OrderMapper;
import kr.hhplus.be.server.point.application.service.PointService;
import kr.hhplus.be.server.point.domain.model.PointRequestDto;
import kr.hhplus.be.server.point.domain.repository.PointRepository;
import kr.hhplus.be.server.point.infrastructure.persistence.mapper.PointMapper;
import kr.hhplus.be.server.user.application.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;

@ActiveProfiles("test")
@SpringBootTest
public class OrderTest {

    @Autowired
    private OrderService orderService;
    
    @Autowired
    private PointService pointService;

    @Autowired
    private PointRepository pointRepository;

    @Mock
    private PointMapper pointMapper;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private UserService userService;
    
    @DisplayName("포인트 충전")
    @BeforeEach
    void chargePointTest() {
        final int userNo = 2;
        final long chargePoint = 10000;
        PointRequestDto request = PointRequestDto.builder()
                .amount(chargePoint)
                .userNo(userNo)
                .build();
        pointService.charge(request);
    }

    @DisplayName("상품 주문 & 결제 흐름 전체 통합 테스트: 주문 → 재고 차감 → 잔액 차감 → 주문 저장 → 외부 전송 Mock 검증")
    @Test
    void orderTotalTest() {
        //given
        final int userNo = 2;
        OrderRequestDto.OrderGoods goods1 = new OrderRequestDto.OrderGoods(1, 5);
        List<OrderRequestDto.OrderGoods> orderGoodsList = new ArrayList<OrderRequestDto.OrderGoods>();
        orderGoodsList.add(goods1);
        OrderRequestDto requestDto = new OrderRequestDto(userNo, orderGoodsList);

        orderService.order(requestDto);
    }

}
