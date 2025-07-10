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
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
    }

    @Override
    public String getMessage() { return message; }
}
