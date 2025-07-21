package kr.hhplus.be.server.coupon;

import kr.hhplus.be.server.coupon.application.service.CouponIssueService;
import kr.hhplus.be.server.coupon.domain.model.CouponIssueRequestDto;
import kr.hhplus.be.server.coupon.domain.repository.CouponIssueRepository;
import kr.hhplus.be.server.user.application.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CouponIssueValidationTest {

    @InjectMocks
    private CouponIssueService couponIssueService;

    @Mock
    private CouponIssueRepository couponIssueRepository;

    @Mock
    private UserService userService;

    @DisplayName("쿠폰이 존재하지만 발급되지 않았다면 false를 반환한다")
    @Test
    void doesNotIssue() {
        final String couponId = "ABC";
        final int userNo = 10;

        // given
        CouponIssueRequestDto requestDto = new CouponIssueRequestDto(couponId, userNo);
        given(userService.checkUserCountByUserNo(userNo)).willReturn(1);

        // when
        boolean result = couponIssueService.validateCouponIssueRequest(requestDto);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("쿠폰이 존재하지 않으면 false를 반환한다")
    @Test
    void notExistCoupon() {
        final String couponId = "noname";
        final int userNo = 10;

        // given
        CouponIssueRequestDto requestDto = new CouponIssueRequestDto(couponId, userNo);
        given(userService.checkUserCountByUserNo(userNo)).willReturn(1);

        // when
        boolean result = couponIssueService.validateCouponIssueRequest(requestDto);

        // then
        assertThat(result).isFalse();
    }

    @DisplayName("쿠폰이 이미 발급되었으면 false를 반환한다")
    @Test
    void alreadyIssue() {
        final String couponId = "ABC";
        final int userNo = 100;

        // given
        CouponIssueRequestDto requestDto = new CouponIssueRequestDto(couponId, userNo);
        given(userService.checkUserCountByUserNo(userNo)).willReturn(1);

        // when
        boolean result = couponIssueService.validateCouponIssueRequest(requestDto);

        // then
        assertThat(result).isFalse();
    }
}
