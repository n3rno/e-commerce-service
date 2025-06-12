package kr.hhplus.be.server.point.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PointRequestDto {
    long amount;        // 충전금액
    int userNo;        // 유저번호
    String orderId;        // 유저번호
}
