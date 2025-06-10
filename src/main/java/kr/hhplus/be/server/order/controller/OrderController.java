package kr.hhplus.be.server.order.controller;

import kr.hhplus.be.server.order.domain.model.OrderRequestDto;
import kr.hhplus.be.server.order.application.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    public ResponseEntity order(@RequestBody OrderRequestDto orderRequestDto) {
        orderService.order(orderRequestDto);
        return ResponseEntity.ok(null);
    }

}
