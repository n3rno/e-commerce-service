package kr.hhplus.be.server.point.infrastructure.persistence;

import kr.hhplus.be.server.point.domain.model.Point;
import kr.hhplus.be.server.point.domain.model.PointBalance;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface PointMapper {
    Optional<PointBalance> selectBalanceByUserNo(int userNo);
    void insertPointHist(Point point);
}
