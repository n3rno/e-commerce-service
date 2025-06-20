package kr.hhplus.be.server.order.domain.service;

import kr.hhplus.be.server.util.DateStringUtill;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OrderIdGenerator {
    public String generate() {

        return "E" + DateStringUtill.dateToStringYyMMddHHmmss() + "_" + generateShortUniqueId();
    }

    private static String generateShortUniqueId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 5); // 5자리만 추출
    }
}
