package kr.hhplus.be.server.point;

import kr.hhplus.be.server.point.model.PointBalance;
import kr.hhplus.be.server.point.service.PointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class PointServiceTest {

    // TODO 서비스

    @DisplayName("쿼리 조회 결과가 null인 경우, 0원을 반환한다.")
    @Test
    void ifNullThenReturnZeroBalance() {
        //given
        final long userNo = 0;

        //when
        PointBalance balance = pointService.getBalance(userNo);

        //then
        assertThat(balance.getBalance()).isEqualTo(0);

    }
}
