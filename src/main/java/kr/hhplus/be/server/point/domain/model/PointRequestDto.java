package kr.hhplus.be.server.point.domain.model;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PointRequestDto {
    @Positive(message = "충전 금액은 1000원 이상이어야 합니다.")
    long amount;        // 충전금액
    int userNo;        // 유저번호
    String orderId;        // 유저번호
}
