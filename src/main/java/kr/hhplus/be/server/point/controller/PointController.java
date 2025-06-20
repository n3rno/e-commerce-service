package kr.hhplus.be.server.point.controller;

import kr.hhplus.be.server.point.domain.model.PointBalance;
import kr.hhplus.be.server.point.domain.model.PointRequestDto;
import kr.hhplus.be.server.point.application.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/point")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;

    @PostMapping("/balance")
    public ResponseEntity<PointBalance> balance(@RequestParam int userNo) {
        PointBalance point = pointService.selectBalance(userNo);
        return ResponseEntity.ok(point);
    }

    // TODO 잔액을 return 한다.
    @PostMapping("/charge")
    public ResponseEntity charge(@RequestBody PointRequestDto requestDto) {
        pointService.charge(requestDto);

        return ResponseEntity.ok(null);
    }
}
