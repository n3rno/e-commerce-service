package kr.hhplus.be.server.point.repository;

import kr.hhplus.be.server.point.model.Point;
import kr.hhplus.be.server.point.model.PointBalance;

import java.util.Optional;

public interface PointDao {
    Optional<PointBalance> selectBalance(int userNo);
    void insertPointHist(Point point);
}
