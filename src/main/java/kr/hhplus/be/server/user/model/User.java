package kr.hhplus.be.server.user.model;

import kr.hhplus.be.server.point.enums.PointType;
import lombok.Builder;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

public class User {
    int no;            // pk
    String username;
    String password;
    LocalDateTime createdAt;
}
