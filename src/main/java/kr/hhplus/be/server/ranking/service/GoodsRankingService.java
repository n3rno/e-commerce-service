package kr.hhplus.be.server.ranking.service;

import kr.hhplus.be.server.ranking.domain.GoodsRankDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GoodsRankingService {

    private static final String RANKING_KEY = "goods:ranking";

    private final int rankCount = 10;

    private final StringRedisTemplate redisTemplate;

    // 주문 시 랭킹 증가
    public void increaseGoodsScore(int goodsNo, long count) {
        redisTemplate.opsForZSet().incrementScore(RANKING_KEY, String.valueOf(goodsNo), count);
    }

    // 랭킹 조회
    public List<GoodsRankDto> getTopRankedProducts() {
        Set<ZSetOperations.TypedTuple<String>> rankedSet = redisTemplate.opsForZSet()
                .reverseRangeWithScores(RANKING_KEY, 0, rankCount - 1);

        if (rankedSet == null) return Collections.emptyList();

        List<GoodsRankDto> result = new ArrayList<>();
        int rank = 1;

        for (ZSetOperations.TypedTuple<String> tuple : rankedSet) {
            result.add(new GoodsRankDto(rank++, tuple.getValue(), tuple.getScore().longValue()));
        }

        return result;
    }


}
