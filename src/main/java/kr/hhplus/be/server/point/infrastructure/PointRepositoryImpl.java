package kr.hhplus.be.server.point.infrastructure;

import kr.hhplus.be.server.point.domain.model.Point;
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
    public void insertPointHist(Point point) {
        pointMapper.insertPointHist(point);
    }
}
