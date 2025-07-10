package kr.hhplus.be.server.ranking.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GoodsRankDto {
    private int rank;
    private String goodsNo;
    private long score;
}
