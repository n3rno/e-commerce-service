package kr.hhplus.be.server.coupon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class CouponIssue {

    	private int no;
		private String couponId;
		private Integer userNo;
		private String useYn;
		private LocalDateTime createdAt;

}
