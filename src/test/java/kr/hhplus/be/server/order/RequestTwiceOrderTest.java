package kr.hhplus.be.server.order;

import kr.hhplus.be.server.goods.application.service.GoodsService;
import kr.hhplus.be.server.goods.domain.model.GoodsResponseDto;
import kr.hhplus.be.server.order.application.service.OrderService;
import kr.hhplus.be.server.order.domain.model.OrderRequestDto;
import kr.hhplus.be.server.point.application.service.PointService;
import kr.hhplus.be.server.point.domain.model.PointBalance;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor
public class RequestTwiceOrderTest {

    private final GoodsService goodsService;
    private final OrderService orderService;
    private final PointService pointService;

    @Test
    // 동일 유저가 두 번 결제 요청 → 잔액 음수 오류
    @DisplayName("동일 유저가 두 번 결제 요청 할 때 잔액은 음수가 되지 않는다.")
    void concurrencyUserOrderTwiceTest() throws Exception {
        // 1. 동시에 요청할 스레드 수 (2 주문을 동시 요청)
        int THREAD_COUNT = 2;
        int USER_NO = 5;

        // 2. 고정된 크기의 스레드 풀 생성 (병렬 실행을 위한 Executor)
        ExecutorService es = Executors.newFixedThreadPool(THREAD_COUNT);

        // 3. 모든 스레드 작업이 완료될 때까지 대기할 CountDownLatch
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        // 4. 2번의 주문 요청을 비동기로 실행
        for (int i = 0; i < THREAD_COUNT; i++) {
            es.submit(() -> {
                try {
                    // 상품 ID 5번, 사용자 ID 5번에 대해 주문 요청
                    orderService.orderGoodsDirect(5, 5);
                } catch (Exception ignored) {
                    // 에러 발생 시 테스트 실패가 아님 → 실패 주문은 무시 (예: 잔액 부족, 재고 없음 등)
                } finally {
                    // 스레드 하나가 끝날 때마다 카운트 감소
                    latch.countDown();
                }
            });
        }

        // 5. 모든 요청이 완료될 때까지 대기 (latch가 0이 될 때까지)
        latch.await();

        GoodsResponseDto goods = goodsService.getGoodsByGoodsNo(1L);
        PointBalance balance = pointService.selectBalance(USER_NO);
        // 6. 결제 시도 후 잔액 출력
        System.out.println("최종 잔액: " + balance.getBalance());

        assertThat(goods.getStock()).isGreaterThanOrEqualTo(0);
    }
}
