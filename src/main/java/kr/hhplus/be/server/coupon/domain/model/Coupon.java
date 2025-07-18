package kr.hhplus.be.server.coupon.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class Coupon {

    	private String id;
		private long discountPrice;
		private int maxQuantity;
		private int nowQuantity;
		private int minimumOrderAmount;
		private LocalDateTime createdAt;

}
