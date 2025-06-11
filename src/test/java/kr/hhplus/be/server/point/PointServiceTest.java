package kr.hhplus.be.server.point;

import kr.hhplus.be.server.point.application.service.PointService;
import kr.hhplus.be.server.point.domain.model.Point;
import kr.hhplus.be.server.point.domain.model.PointBalance;
import kr.hhplus.be.server.point.domain.model.enums.PointType;
import kr.hhplus.be.server.point.domain.repository.PointRepository;
import kr.hhplus.be.server.point.infrastructure.persistence.mapper.PointMapper;
import kr.hhplus.be.server.user.application.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        PointBalance balance = pointService.selectBalance(userNo);

        Point point = Point.builder()
                .type(PointType.CHARGE)
                .amount(chargeAmount)
                .balance(balance.getBalance() + chargeAmount)
                .userNo(userNo)
        .build();

        // 포인트 충전 이력 생성
        pointRepository.insertPointHist(point);
    }

}
