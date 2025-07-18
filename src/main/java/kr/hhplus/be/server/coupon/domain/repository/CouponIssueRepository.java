package kr.hhplus.be.server.coupon.domain.repository;

import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.CouponIssue;
import kr.hhplus.be.server.coupon.domain.model.CouponIssueRequestDto;

import java.util.List;

public interface CouponIssueRepository {

    int insertCouponIssue(CouponIssue couponIssue);

    CouponIssue selectCouponAndIssuable(CouponIssueRequestDto requestDto);

    void save(CouponIssue couponIssue);

    List<CouponIssue> findAllByCouponId(String couponId);
}
