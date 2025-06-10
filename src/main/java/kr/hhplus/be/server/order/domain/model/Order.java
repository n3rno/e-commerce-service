package kr.hhplus.be.server.order.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class Order {
    int userNo;
    String id;
    long totalOrderAmount;
    Integer couponIssueNo;
    LocalDateTime createdAt;
}
