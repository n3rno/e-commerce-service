package kr.hhplus.be.server.point.application.service;

import kr.hhplus.be.server.point.domain.model.enums.PointIdempotencyType;
import kr.hhplus.be.server.point.domain.model.enums.PointType;
import kr.hhplus.be.server.point.domain.model.PointHist;
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
        String idempotencyKey = PointHist.makeIndempotencyKey(PointIdempotencyType.CHARGE);
        if (pointRepository.countIndempotencyKey(idempotencyKey, request.getUserNo()) > 0) {
            throw new IllegalStateException("Already processed request");
        }

        // 잔액 조회 (FOR UPDATE 적용)
        long balance = findByUserIdForUpdate(request.getUserNo());

        PointHist pointHist = PointHist.builder()
                .type(PointType.CHARGE)
                .amount(request.getAmount())
                .balance(balance + request.getAmount())
                .userNo(request.getUserNo())
                .idempotencyKey(idempotencyKey)
        .build();

        // 포인트 충전 이력 생성
        pointRepository.updatePoint(request.getUserNo(), balance + request.getAmount());
        pointRepository.insertPointHist(pointHist);
    }

    public void use(PointRequestDto request, PointIdempotencyType type) {
        // 멱등키 확인
        String idempotencyKey = PointHist.makeIndempotencyKey(type);
        if (pointRepository.countIndempotencyKey(idempotencyKey, request.getUserNo()) > 0) {
            throw new IllegalStateException("Already processed request");
        }

        // 잔액 조회
        // 유저 포인트 잔액 조회 시 FOR UPDATE 적용
        // → 동시에 다른 결제 트랜잭션이 이 유저의 잔액을 조회하지 못하도록 막음
        long balance = findByUserIdForUpdate(request.getUserNo());
        // 검증 로직: 잔액 부족한 경우 예외 발생
        if (balance < request.getAmount()) {
            throw new IllegalArgumentException("Not Enough Balance");
        }

        PointHist pointHist = PointHist.builder()
                .type(PointType.USE)
                .amount(request.getAmount())
                .balance(balance - request.getAmount())
                .userNo(request.getUserNo())
                .idempotencyKey(idempotencyKey)
                .orderId(request.getOrderId())
                .build();

        // 포인트 차감 이력 생성
        pointRepository.updatePoint(request.getUserNo(), balance - request.getAmount());
        pointRepository.insertPointHist(pointHist);
    }

    // 포인트 차감을 위한 잔액 조회 (배타락)
    public long findByUserIdForUpdate(int userNo) {
        // 사용자 존재 여부 확인
        if (0 == userService.checkUserCountByUserNo(userNo)) {
            throw new IllegalArgumentException("Not Exist User");
        }

        return pointRepository.findByUserIdForUpdate(userNo)
                .orElse(0L);
    }

}
