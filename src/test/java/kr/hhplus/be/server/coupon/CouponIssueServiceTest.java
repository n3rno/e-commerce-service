package kr.hhplus.be.server.coupon;

import kr.hhplus.be.server.coupon.application.service.CouponIssueService;
import kr.hhplus.be.server.coupon.domain.model.Coupon;
import kr.hhplus.be.server.coupon.domain.model.CouponIssue;
import kr.hhplus.be.server.coupon.domain.model.CouponIssueRequestDto;
import kr.hhplus.be.server.coupon.domain.repository.CouponIssueRepository;
import kr.hhplus.be.server.coupon.domain.repository.CouponRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class CouponIssueServiceTest {
    @Autowired
    private CouponIssueService couponIssueService;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private CouponIssueRepository couponIssueRepository;

    final String couponId = "TEST_COUPON";

    @BeforeEach
    void setup() {
        couponRepository.save(new Coupon(couponId, 1000, 100, 100, 0, LocalDateTime.now()));
    }

    @Test
    @DisplayName("동시에 150명 요청 시 100명만 쿠폰 발급된다.")
    void issueTest() throws InterruptedException {
        int threadCount = 150;
        int startUserNo = 1000;
        ExecutorService executor = Executors.newFixedThreadPool(20);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = startUserNo; i <= startUserNo+threadCount; i++) {
            final int userNo = i;
            executor.submit(() -> {
                couponIssueService.issueCoupon(CouponIssueRequestDto.builder()
                        .couponId(couponId)
                        .userNo(userNo).build());
                latch.countDown();
            });
        }

        latch.await();

        List<CouponIssue> issued = couponIssueRepository.findAllByCouponId(couponId);
        assertThat(issued).hasSize(100);
    }
}
