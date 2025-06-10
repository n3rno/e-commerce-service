package kr.hhplus.be.server.point.domain.repository;

import kr.hhplus.be.server.point.domain.model.Point;
import kr.hhplus.be.server.point.domain.model.PointBalance;

import java.util.Optional;

public interface PointRepository {

    Optional<PointBalance> selectBalanceByUserNo(int userNo);
    void insertPointHist(Point point);
}
