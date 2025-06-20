package kr.hhplus.be.server.order.domain.service;

import kr.hhplus.be.server.util.DateStringUtill;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class OrderIdGenerator {
    public String generate() {
        return "E" + DateStringUtill.dateToStringYyMMddHHmmss();
    }
}
