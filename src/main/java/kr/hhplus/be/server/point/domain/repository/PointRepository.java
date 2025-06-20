package kr.hhplus.be.server.point.domain.repository;

import kr.hhplus.be.server.point.domain.model.PointHist;
import kr.hhplus.be.server.point.domain.model.PointBalance;

import java.util.Optional;

public interface PointRepository {

    Optional<PointBalance> selectBalanceByUserNo(int userNo);
    void insertPointHist(PointHist point);
    int countIndempotencyKey(String key, int userNo);
    Optional<Long> findByUserIdForUpdate(int userNo);
    void updatePoint(int userNo, long point);
}
