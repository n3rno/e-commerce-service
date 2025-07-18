package kr.hhplus.be.server.coupon.infrastructure.persistence.mapper;

import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.CouponIssue;
import kr.hhplus.be.server.coupon.domain.model.CouponIssueRequestDto;
import kr.hhplus.be.server.goods.domain.model.GoodsResponseDto;
import kr.hhplus.be.server.order.domain.model.OrderRequestDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CouponMapper {

    int insertCouponIssue(CouponIssue couponIssue);

    CouponIssue selectCouponAndIssuable(CouponIssueRequestDto requestDto);

    Optional<Coupon> selectCouponInfoById(String couponId);

    void insertCoupon(Coupon coupon);

    List<CouponIssue> selectAllByCouponId(String couponId);
}
