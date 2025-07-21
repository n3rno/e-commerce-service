package kr.hhplus.be.server.coupon.controller;

import kr.hhplus.be.server.coupon.application.service.CouponIssueService;
import kr.hhplus.be.server.coupon.domain.model.CouponIssueRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
public class CouponController {

    private final CouponIssueService couponIssueService;

    @PostMapping("/issue")
    public ResponseEntity<Boolean> issueCoupon(@RequestBody CouponIssueRequestDto requestDto) {
        Boolean success = couponIssueService.issueCoupon(requestDto);
        return ResponseEntity.ok(success);
    }

}
