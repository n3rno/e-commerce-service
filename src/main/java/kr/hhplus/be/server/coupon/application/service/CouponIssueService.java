package kr.hhplus.be.server.coupon.application.service;

import kr.hhplus.be.server.Exception.EcommerceException;
import kr.hhplus.be.server.Exception.ErrorCode;
import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.CouponIssue;
import kr.hhplus.be.server.coupon.domain.model.CouponIssueRequestDto;
import kr.hhplus.be.server.coupon.domain.repository.CouponIssueRepository;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import kr.hhplus.be.server.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class CouponIssueService {

    private final RedisTemplate<String, String> redisTemplate;
    private final CouponIssueRepository couponIssueRepository;
    private final CouponRepository couponRepository;
    private final UserService userService;

    private static final String ISSUE_COUNT_KEY_PREFIX = "coupon:issued:";
    private static final String USER_SET_KEY_PREFIX = "coupon:user:";

    @Transactional
    public Boolean issueCoupon(CouponIssueRequestDto requestDto) {
        String countKey = ISSUE_COUNT_KEY_PREFIX + requestDto.getCouponId();
        String userSetKey = USER_SET_KEY_PREFIX + requestDto.getCouponId();

        // 이미 발급된 사용자 방지 (Redis Set)
        Boolean alreadyIssued = redisTemplate.opsForSet().isMember(userSetKey, requestDto.getUserNo());
        if (Boolean.TRUE.equals(alreadyIssued)) {
            return false; // 이미 발급받음
        }

        // request 정보가 유효한지 확인
        if (!validateCouponIssueRequest(requestDto)) {
            return false;
        }

        // 최대 수량 캐시 초기화
        if (Boolean.FALSE.equals(redisTemplate.hasKey(countKey))) {
            Coupon coupon = couponRepository.findById(requestDto.getCouponId())
                    .orElseThrow(() -> new EcommerceException(ErrorCode.NOT_EXIST_COUPON));
            redisTemplate.opsForValue().set(countKey, "0");
            redisTemplate.expire(countKey, Duration.ofHours(1));
        }

        // 수량 증가 후 최대 수량 초과 확인
        Long currentCount = redisTemplate.opsForValue().increment(countKey);
        int maxCount = couponRepository.findById(requestDto.getCouponId())
                .map(Coupon::getMaxQuantity)
                .orElseThrow(() -> new EcommerceException(ErrorCode.NOT_ENOUGH_COUPON));

        if (currentCount > maxCount) {
            return false;// 수량 초과
        }

        // 발급 처리
        redisTemplate.opsForSet().add(userSetKey, String.valueOf(requestDto.getUserNo()));
        // 쿠폰 발급
        couponIssueRepository.save(CouponIssue.builder()
                .couponId(requestDto.getCouponId())
                .userNo(requestDto.getUserNo())
                .useYn("N").build());

        return true;
    }

    public boolean validateCouponIssueRequest(CouponIssueRequestDto requestDto) {
        // 존재하는 회원인지 확인
        if (0 == userService.checkUserCountByUserNo(requestDto.getUserNo())) {
            throw new EcommerceException(ErrorCode.NOT_EXIST_USER);
        }

        CouponIssue couponIssue = couponIssueRepository.selectCouponAndIssuable(requestDto);
        // 존재하는 쿠폰이 아님 -> return false
        if (couponIssue == null) return false;

        // 발급 받지 않은 쿠폰임 -> return true
        return couponIssue.getUserNo() == null;

    }
}
