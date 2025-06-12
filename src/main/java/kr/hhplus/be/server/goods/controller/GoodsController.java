package kr.hhplus.be.server.goods.controller;

import kr.hhplus.be.server.goods.application.service.GoodsService;
import org.springframework.web.bind.annotation.RestController;

import kr.hhplus.be.server.goods.domain.model.GoodsResponseDto;
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
        GoodsResponseDto goods = goodsService.getGoodsByGoodsNo(goodsNo);
        return ResponseEntity.ok(goods);
    }

}
