package kr.hhplus.be.server.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateStringUtill {
    public static String dateToStringYyMMddHHmmss() {
        return LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyMMddHHmmss"));
    }
}
