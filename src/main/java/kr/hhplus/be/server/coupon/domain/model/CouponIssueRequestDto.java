package kr.hhplus.be.server.coupon.domain.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class CouponIssueRequestDto {

		private String couponId;
		private int userNo;

}
