package kr.hhplus.be.server.point.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class PointBalance {
    long balance;       // 잔액
    int userNo;         // 유저번호
}
