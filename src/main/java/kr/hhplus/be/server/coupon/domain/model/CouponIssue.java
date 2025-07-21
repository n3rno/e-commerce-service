package kr.hhplus.be.server.coupon.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CouponIssue {

    	private int no;
		private String couponId;
		private Integer userNo;
		private String useYn;
		private LocalDateTime createdAt;

}
