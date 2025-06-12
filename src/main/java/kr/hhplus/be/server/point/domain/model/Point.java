package kr.hhplus.be.server.point.domain.model;

import kr.hhplus.be.server.point.domain.model.enums.PointIdempotencyType;
import kr.hhplus.be.server.point.domain.model.enums.PointType;
import kr.hhplus.be.server.util.DateStringUtill;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class Point {
    long no;            // pk
    PointType type;     // 유형 - 충전, 차감
    long amount;        // 충전(사용)금액
    String orderId;     // 주문번호
    long balance;       // 잔액
    int userNo;        // 유저번호
    LocalDateTime createdAt; // 등록일
    String idempotencyKey; // 멱등키


    public static String makeIndempotencyKey(PointIdempotencyType type) {
        return type + DateStringUtill.dateToStringYyMMddHHmmss();
    }
}
