package kr.hhplus.be.server.point.infrastructure.persistence.mapper;

import kr.hhplus.be.server.point.domain.model.PointHist;
import kr.hhplus.be.server.point.domain.model.PointBalance;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface PointMapper {
    Optional<PointBalance> selectBalanceByUserNo(int userNo);
    void insertPointHist(PointHist point);
    int countIndempotencyKey(String key, int userNo);
    Optional<Long> findByUserIdForUpdate(int userNo);
    void upsertPoint(int userNo, long point);
}
