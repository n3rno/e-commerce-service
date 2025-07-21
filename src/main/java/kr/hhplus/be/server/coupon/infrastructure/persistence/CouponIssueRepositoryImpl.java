package kr.hhplus.be.server.coupon.infrastructure.persistence;

import kr.hhplus.be.server.coupon.domain.model.CouponIssue;
import kr.hhplus.be.server.coupon.domain.model.CouponIssueRequestDto;
import kr.hhplus.be.server.coupon.domain.repository.CouponIssueRepository;
import kr.hhplus.be.server.coupon.infrastructure.persistence.mapper.CouponMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class CouponIssueRepositoryImpl implements CouponIssueRepository {
    private final CouponMapper couponMapper;

    @Override
    public int insertCouponIssue(CouponIssue couponIssue) {
        return couponMapper.insertCouponIssue(couponIssue);
    }

    @Override
    public CouponIssue selectCouponAndIssuable(CouponIssueRequestDto requestDto) {
        return couponMapper.selectCouponAndIssuable(requestDto);
    }

    @Override
    public void save(CouponIssue couponIssue) {
        couponMapper.insertCouponIssue(couponIssue);
    }

    @Override
    public List<CouponIssue> findAllByCouponId(String couponId) {
        return couponMapper.selectAllByCouponId(couponId);
    }
}
