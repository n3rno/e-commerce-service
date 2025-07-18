package kr.hhplus.be.server.coupon.domain.repository;

import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.CouponIssue;
import kr.hhplus.be.server.coupon.domain.model.CouponIssueRequestDto;

import java.util.Optional;

public interface CouponRepository {

    Optional<Coupon> findById(String couponId);

    void save(Coupon coupon);
}
