package kr.hhplus.be.server.point.service;

import kr.hhplus.be.server.point.enums.PointType;
import kr.hhplus.be.server.point.model.Point;
import kr.hhplus.be.server.point.model.PointBalance;
import kr.hhplus.be.server.point.model.PointChargeRequestDto;
import kr.hhplus.be.server.point.repository.PointDao;
import kr.hhplus.be.server.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserService userService;
    private final PointDao pointDao;

    public PointBalance selectBalance(int userNo) {
        
        // 사용자 존재 여부 확인
        if (0 == userService.checkUserCountByUserNo(userNo)) {
            throw new IllegalArgumentException("Not Exist User");
        };

        // 이력이 없는 경우 0원 리턴
        return pointDao.selectBalance(userNo).orElse(
                PointBalance.builder()
                .balance(0)
                .userNo(userNo).build());
    }

    public void charge(PointChargeRequestDto request) {

        // 잔액 조회
        PointBalance balance = selectBalance(request.getUserNo());

        Point point = Point.builder()
                .type(PointType.CHARGE)
                .amount(request.getAmount())
                .balance(balance.getBalance() + request.getAmount())
                .userNo(request.getUserNo())
        .build();

        // 포인트 충전 이력 생성
        pointDao.insertPointHist(point);

    }

}
