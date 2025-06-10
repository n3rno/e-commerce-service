package kr.hhplus.be.server.point;

import kr.hhplus.be.server.point.domain.model.PointBalance;
import kr.hhplus.be.server.point.infrastructure.persistence.PointMapper;
import kr.hhplus.be.server.point.application.service.PointService;
import kr.hhplus.be.server.user.application.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class PointServiceTest {

    // TODO 맞는지 확인
    @InjectMocks
    private PointService pointService;

    @Mock
    private PointMapper pointMapper;

    @Mock
    private UserService userService;

    @DisplayName("쿼리 조회 결과가 null인 경우, 0원을 반환한다.")
    @Test
    void ifNullThenReturnZeroBalance() {
        //given
        final int userNo = 0;

        //when
        PointBalance balance = pointService.selectBalance(userNo);

        //then
        assertThat(balance.getBalance()).isEqualTo(0);

    }
}
