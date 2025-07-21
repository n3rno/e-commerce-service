package kr.hhplus.be.server.coupon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Getter
@Builder
public class CouponIssueRequestDto {

		private String couponId;
		private int userNo;

}
