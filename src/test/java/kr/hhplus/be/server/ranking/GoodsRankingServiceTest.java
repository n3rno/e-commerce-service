package kr.hhplus.be.server.ranking;

import kr.hhplus.be.server.point.application.service.PointService;
import kr.hhplus.be.server.point.domain.model.PointBalance;
import kr.hhplus.be.server.point.domain.model.PointHist;
import kr.hhplus.be.server.point.domain.model.PointRequestDto;
import kr.hhplus.be.server.point.domain.model.enums.PointIdempotencyType;
import kr.hhplus.be.server.point.domain.model.enums.PointType;
import kr.hhplus.be.server.point.domain.repository.PointRepository;
import kr.hhplus.be.server.point.infrastructure.persistence.mapper.PointMapper;
import kr.hhplus.be.server.ranking.domain.GoodsRankDto;
import kr.hhplus.be.server.ranking.service.GoodsRankingService;
import kr.hhplus.be.server.user.application.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
public class GoodsRankingServiceTest {

    @Autowired
    private GoodsRankingService goodsRankingService;

    @Autowired
    StringRedisTemplate redisTemplate;

    private final String RANKING_KEY = "goods:ranking";

    @BeforeEach
    void setUp() {
        // 초기화
        redisTemplate.delete(RANKING_KEY);
    }

    @AfterEach
    void tearDown() {
        redisTemplate.delete(RANKING_KEY);
    }

    @DisplayName("주문 시 상품 카운트가 증가한다.")
    @Test
    void redisIncreaseGoodsCountTest() {
        // given
        int productId = 5;

        // when
        goodsRankingService.increaseGoodsScore(productId, 3);

        // then
        Double score = redisTemplate.opsForZSet().score(RANKING_KEY, String.valueOf(productId));
        assertThat(score).isEqualTo(3.0);
    }

    @DisplayName("상품랭킹 top10을 조회한다.")
    @Test
    void getTop10Test() {
        // given
        goodsRankingService.increaseGoodsScore(5, 10);
        goodsRankingService.increaseGoodsScore(3, 2);

        // when
        List<GoodsRankDto> top10 = goodsRankingService.getTopRankedProducts();

        // then
        assertThat(top10.get(0).getGoodsNo()).isEqualTo("5");
        assertThat(top10.get(1).getGoodsNo()).isEqualTo("3");
        // 랭킹을 검증
        assertThat(top10.get(0).getRank()).isEqualTo(1); 
    }

}
