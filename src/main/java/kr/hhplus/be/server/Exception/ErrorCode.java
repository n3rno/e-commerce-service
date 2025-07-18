package kr.hhplus.be.server.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

//    NOT_EXIST("0001", "존재하지 않습니다."),
    LACK_OF_STOCK("0100", "재고가 부족합니다."),
    REQUEST_LOCKED("0101", "다른 요청이 처리 중입니다."),
    NOT_EXIST_USER("0102", "존재하지 않는 회원입니다."),
    NOT_EXIST_GOODS("0103", "존재하지 않는 상품입니다."),
    REQUEST_ALREADY_IN_PROGRESS("0104", "요청이 이미 진행중입니다."),
    NOT_ENOUGH_BALANCE("0105", "잔액이 부족합니다."),
    IMPOSSIBLE_ORDER("0106", "주문할 수 없는 상태입니다."),
    NOT_EXIST_COUPON("0107", "존재하지 않는 쿠폰 정보입니다."),
    NOT_ENOUGH_COUPON("0108", "쿠폰이 소진되었습니다."),
    ;

    private final String code;
    private final String message;
}
