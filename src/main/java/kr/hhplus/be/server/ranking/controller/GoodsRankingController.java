package kr.hhplus.be.server.ranking.controller;

import kr.hhplus.be.server.ranking.domain.GoodsRankDto;
import kr.hhplus.be.server.ranking.service.GoodsRankingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/goods/ranking")
@RequiredArgsConstructor
public class GoodsRankingController {

    private final GoodsRankingService goodsRankingService;

    @PostMapping("/top10")
    public ResponseEntity<List<GoodsRankDto>> top10() {
        return ResponseEntity.ok(goodsRankingService.getTopRankedProducts());
    }

}
