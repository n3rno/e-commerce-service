package kr.hhplus.be.server.coupon.infrastructure.persistence;

import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.CouponIssue;
import kr.hhplus.be.server.coupon.domain.model.CouponIssueRequestDto;
import kr.hhplus.be.server.coupon.domain.repository.CouponIssueRepository;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.coupon.infrastructure.persistence.mapper.CouponMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class CouponRepositoryImpl implements CouponRepository {
    private final CouponMapper couponMapper;

    @Override
    public Optional<Coupon> findById(String couponId) {
        return couponMapper.selectCouponInfoById(couponId);
    }

    @Override
    public void save(Coupon coupon) {
        couponMapper.insertCoupon(coupon);
    }

}
