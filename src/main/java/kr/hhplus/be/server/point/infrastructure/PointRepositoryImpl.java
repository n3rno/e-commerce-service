package kr.hhplus.be.server.point.infrastructure;

import kr.hhplus.be.server.point.domain.model.PointHist;
import kr.hhplus.be.server.point.domain.model.PointBalance;
import kr.hhplus.be.server.point.domain.repository.PointRepository;
import kr.hhplus.be.server.point.infrastructure.persistence.mapper.PointMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {

    private final PointMapper pointMapper;

    @Override
    public Optional<PointBalance> selectBalanceByUserNo(int userNo) {
        return pointMapper.selectBalanceByUserNo(userNo);
    }

    @Override
    public void insertPointHist(PointHist point) {
        pointMapper.insertPointHist(point);
    }

    @Override
    public int countIndempotencyKey(String key, int userNo) {
        return pointMapper.countIndempotencyKey(key, userNo);
    }

    @Override
    public Optional<Long> findByUserIdForUpdate(int userNo) {
        return pointMapper.findByUserIdForUpdate(userNo);
    }

    @Override
    public void updatePoint(int userNo, long point) {
        pointMapper.upsertPoint(userNo, point);
    }
}
