package kr.hhplus.be.server.point.application.service;

import kr.hhplus.be.server.point.domain.model.enums.PointIdempotencyType;
import kr.hhplus.be.server.point.domain.model.enums.PointType;
import kr.hhplus.be.server.point.domain.model.Point;
import kr.hhplus.be.server.point.domain.model.PointBalance;
import kr.hhplus.be.server.point.domain.model.PointRequestDto;
import kr.hhplus.be.server.point.domain.repository.PointRepository;
import kr.hhplus.be.server.user.application.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserService userService;
    private final PointRepository pointRepository;

    public PointBalance selectBalance(int userNo) {
        
        // 사용자 존재 여부 확인
        if (0 == userService.checkUserCountByUserNo(userNo)) {
            throw new IllegalArgumentException("Not Exist User");
        }

        // 이력이 없는 경우 0원 리턴
        return pointRepository.selectBalanceByUserNo(userNo).orElse(
                PointBalance.builder()
                .balance(0)
                .userNo(userNo).build());
    }

    public void charge(PointRequestDto request) {
        // 멱등키 확인
        String idempotencyKey = Point.makeIndempotencyKey(PointIdempotencyType.CHARGE);
        if (pointRepository.countIndempotencyKey(idempotencyKey, request.getUserNo()) > 0) {
            throw new IllegalStateException("Already processed request");
        }

        // 잔액 조회
        PointBalance balance = selectBalance(request.getUserNo());

        Point point = Point.builder()
                .type(PointType.CHARGE)
                .amount(request.getAmount())
                .balance(balance.getBalance() + request.getAmount())
                .userNo(request.getUserNo())
                .idempotencyKey(idempotencyKey)
        .build();

        // 포인트 충전 이력 생성
        pointRepository.insertPointHist(point);
    }

    public void use(PointRequestDto request, PointIdempotencyType type) {
        // 멱등키 확인
        String idempotencyKey = Point.makeIndempotencyKey(type);
        if (pointRepository.countIndempotencyKey(idempotencyKey, request.getUserNo()) > 0) {
            throw new IllegalStateException("Already processed request");
        }

        // 잔액 조회
        PointBalance balance = selectBalance(request.getUserNo());

        // 잔액이 부족하면 차감 불가
        if (balance.getBalance() < request.getAmount()) {
            throw new IllegalArgumentException("Not Enough Balance");
        }

        Point point = Point.builder()
                .type(PointType.USE)
                .amount(request.getAmount())
                .balance(balance.getBalance() - request.getAmount())
                .userNo(request.getUserNo())
                .idempotencyKey(idempotencyKey)
                .orderId(request.getOrderId())
                .build();

        // 포인트 차감 이력 생성
        pointRepository.insertPointHist(point);
    }

}
