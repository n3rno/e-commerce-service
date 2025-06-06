package kr.hhplus.be.server.order.controller;

import kr.hhplus.be.server.order.model.OrderRequestDto;
import kr.hhplus.be.server.order.service.OrderService;
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
