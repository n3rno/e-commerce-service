package kr.hhplus.be.server.point;

import kr.hhplus.be.server.Exception.EcommerceException;
import kr.hhplus.be.server.Exception.ErrorCode;
import kr.hhplus.be.server.point.application.service.PointService;
import kr.hhplus.be.server.point.domain.model.PointHist;
import kr.hhplus.be.server.point.domain.model.PointBalance;
import kr.hhplus.be.server.point.domain.model.PointRequestDto;
import kr.hhplus.be.server.point.domain.model.enums.PointIdempotencyType;
import kr.hhplus.be.server.point.domain.model.enums.PointType;
import kr.hhplus.be.server.point.domain.repository.PointRepository;
import kr.hhplus.be.server.point.infrastructure.persistence.mapper.PointMapper;
import kr.hhplus.be.server.user.application.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class PointServiceTest {

    @Autowired
    private PointService pointService;

    @Autowired
    private PointRepository pointRepository;

    @MockitoBean
    private PointMapper pointMapper;

    @MockitoBean
    private UserService userService;

    @DisplayName("포인트를 10000원 충전한다.")
    @Test
    void chargeTest() {
        final int userNo = 6;
        final long chargeAmount = 300000;

        // 멱등키 확인
        String idempotencyKey = PointHist.makeIndempotencyKey(PointIdempotencyType.TEST);
        if (pointRepository.countIndempotencyKey(idempotencyKey, userNo) > 0) {
            throw new EcommerceException(ErrorCode.REQUEST_ALREADY_IN_PROGRESS);
        }

        PointBalance balance = pointService.selectBalance(userNo);

        PointHist pointHist = PointHist.builder()
                .type(PointType.CHARGE)
                .amount(chargeAmount)
                .balance(balance.getBalance() + chargeAmount)
                .userNo(userNo)
                .idempotencyKey(idempotencyKey)
        .build();

        // 포인트 충전 이력 생성
        pointRepository.updatePoint(userNo, balance.getBalance() + chargeAmount);
        pointRepository.insertPointHist(pointHist);
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
                .isInstanceOf(EcommerceException.class)
                .hasMessage("Already processed request");
    }

}
