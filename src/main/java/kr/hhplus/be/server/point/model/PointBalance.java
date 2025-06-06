package kr.hhplus.be.server.point.model;

import kr.hhplus.be.server.point.enums.PointType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PointBalance {
    long balance;       // 잔액
    int userNo;         // 유저번호
}
