package kr.hhplus.be.server.Exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EcommerceException extends RuntimeException {

    private String code;
    private String message;

    public EcommerceException(ErrorCode errorCode) {
        super(errorCode.getMessage()); // 부모 클리스에 message 값 설정 -> 스택 트레이스 로깅에 유용
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    @Override
    public String getMessage() { return message; }
}
