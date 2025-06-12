package kr.hhplus.be.server.user.domain.model;

import java.time.LocalDateTime;

public class User {
    int no;            // pk
    String username;
    String password;
    LocalDateTime createdAt;
}
