package kr.hhplus.be.server.order;

import kr.hhplus.be.server.goods.application.service.GoodsService;
import kr.hhplus.be.server.goods.domain.model.GoodsResponseDto;
import kr.hhplus.be.server.order.application.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@RequiredArgsConstructor
public class StockOversellTest {

    private final GoodsService goodsService;
    private final OrderService orderService;

    @Test
    // 다수 사용자가 동시에 같은 상품을 주문 → 재고 oversell
    @DisplayName("동시에 100개의 주문요청이 들어올 때 재고는 음수가 되지 않는다.")
    void concurrencyOrderThenStockTest() throws Exception {
        // 1. 동시에 요청할 스레드 수 (100개 주문을 동시 요청)
        int THREAD_COUNT = 100;
        int GOODS_NO = 6;
        int USER_NO = 6;

        // 2. 고정된 크기의 스레드 풀 생성 (병렬 실행을 위한 Executor)
        ExecutorService es = Executors.newFixedThreadPool(THREAD_COUNT);

        // 3. 모든 스레드 작업이 완료될 때까지 대기할 CountDownLatch
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        // 4. 100개의 주문 요청을 비동기로 실행
        for (int i = 0; i < THREAD_COUNT; i++) {
            es.submit(() -> {
                try {
                    // 상품 ID 6번, 사용자 ID 6번에 대해 주문 요청
                    // → 동시에 실행되므로 Race Condition 또는 Deadlock 가능성 테스트
                    orderService.orderGoodsDirect(GOODS_NO, USER_NO); // 수량 1 주문
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

        GoodsResponseDto goods = goodsService.getGoodsByGoodsNo(GOODS_NO);
        // 6. 최종 재고 상태 출력
        System.out.println("최종 재고: " + goods.getStock());

        assertThat(goods.getStock()).isGreaterThanOrEqualTo(0);
        assertThat(goods.getStock()).isEqualTo(0); // 성공한 주문만큼 정확히 줄었는지도 체크
    }
}
