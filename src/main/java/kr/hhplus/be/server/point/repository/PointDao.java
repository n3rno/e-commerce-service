package kr.hhplus.be.server.point.repository;

import kr.hhplus.be.server.point.model.Point;
import kr.hhplus.be.server.point.model.PointBalance;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface PointDao {
    Optional<PointBalance> selectBalance(int userNo);
    void insertPointHist(Point point);
}
