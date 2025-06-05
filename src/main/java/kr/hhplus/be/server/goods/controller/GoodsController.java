package kr.hhplus.be.server.goods.controller;

import kr.hhplus.be.server.goods.model.GoodsResponseDto;
import kr.hhplus.be.server.goods.service.GoodsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/goods")
@RequiredArgsConstructor
public class GoodsController {

    private final GoodsService goodsService;

    @GetMapping("/get/{goodsNo}")
    public ResponseEntity<GoodsResponseDto> selectGoods(@PathVariable long goodsNo) {
        GoodsResponseDto goods = goodsService.selectGoods(goodsNo);
        return ResponseEntity.ok(goods);
    }

}
