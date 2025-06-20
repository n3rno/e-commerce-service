package kr.hhplus.be.server.point;

import kr.hhplus.be.server.order.domain.model.OrderRequestDto;
import kr.hhplus.be.server.point.application.service.PointService;
import kr.hhplus.be.server.point.domain.model.Point;
import kr.hhplus.be.server.point.domain.model.PointBalance;
import kr.hhplus.be.server.point.domain.model.PointRequestDto;
import kr.hhplus.be.server.point.domain.model.enums.PointIdempotencyType;
import kr.hhplus.be.server.point.domain.model.enums.PointType;
import kr.hhplus.be.server.point.domain.repository.PointRepository;
import kr.hhplus.be.server.point.infrastructure.persistence.mapper.PointMapper;
import kr.hhplus.be.server.user.application.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class PointServiceTest {

    @Autowired
    private PointService pointService;

    @Autowired
    private PointRepository pointRepository;

    @Mock
    private PointMapper pointMapper;

    @Mock
    private UserService userService;

    @DisplayName("포인트를 10000원 충전한다.")
    @Test
    void chargeTest() {
        final int userNo = 1;
        final long chargeAmount = 10000;

        // 멱등키 확인
        String idempotencyKey = Point.makeIndempotencyKey(PointIdempotencyType.TEST);
        if (pointRepository.countIndempotencyKey(idempotencyKey, userNo) > 0) {
            throw new IllegalArgumentException("Already processed request");
        }

        PointBalance balance = pointService.selectBalance(userNo);

        Point point = Point.builder()
                .type(PointType.CHARGE)
                .amount(chargeAmount)
                .balance(balance.getBalance() + chargeAmount)
                .userNo(userNo)
                .idempotencyKey(idempotencyKey)
        .build();

        // 포인트 충전 이력 생성
        pointRepository.insertPointHist(point);
    }

    @Test
    @DisplayName("중복 결제 요청은 한 번만 처리되어야 한다.")
    void duplicateOrderShouldBePrevented() {
        final int userNo = 1;

        // given
        PointRequestDto request = PointRequestDto.builder()
                .userNo(userNo)
                .build();

        // when
        pointService.use(request, PointIdempotencyType.TEST);

        // then - 중복 요청 시 예외 발생
        assertThatThrownBy(() -> pointService.use(request, PointIdempotencyType.TEST))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Already processed request");
    }

}
