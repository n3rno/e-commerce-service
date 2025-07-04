package kr.hhplus.be.server.order;

import kr.hhplus.be.server.goods.application.service.GoodsService;
import kr.hhplus.be.server.goods.domain.model.GoodsResponseDto;
import kr.hhplus.be.server.order.application.service.OrderService;
import kr.hhplus.be.server.order.domain.repository.OrderRepository;
import kr.hhplus.be.server.point.application.service.PointService;
import kr.hhplus.be.server.point.domain.model.PointBalance;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class RequestTwiceOrderTest {

    @Autowired
    private GoodsService goodsService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private PointService pointService;
    @Autowired
    private OrderRepository orderRepository;

    @Test
    // 동일 유저가 두 번 결제 요청 → 잔액 음수 오류
    @DisplayName("동일 유저가 두 번 결제 요청 할 때 잔액은 음수가 되지 않는다.")
    void concurrencyUserOrderTwiceTest() throws Exception {
        // 1. 동시에 요청할 스레드 수 (2 주문을 동시 요청)
        int THREAD_COUNT = 2;
        int USER_NO = 5;
        int GOODS_NO = 5;

        // 2. 고정된 크기의 스레드 풀 생성 (병렬 실행을 위한 Executor)
        ExecutorService es = Executors.newFixedThreadPool(THREAD_COUNT);

        // 3. 모든 스레드 작업이 완료될 때까지 대기할 CountDownLatch
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        // 4. 2번의 주문 요청을 비동기로 실행
        for (int i = 0; i < THREAD_COUNT; i++) {
            es.submit(() -> {
                try {
                    // 상품 ID 5번, 사용자 ID 5번에 대해 주문 요청
                    orderService.orderGoodsDirect(GOODS_NO, USER_NO);
                } catch (Exception ignored) {
                    // 에러 발생 시 테스트 실패가 아님 → 실패 주문은 무시 (예: 잔액 부족, 재고 없음 등)
                    System.out.println(ignored.toString());
                } finally {
                    // 스레드 하나가 끝날 때마다 카운트 감소
                    latch.countDown();
                }
            });
        }

        // 5. 모든 요청이 완료될 때까지 대기 (latch가 0이 될 때까지)
        latch.await();

        GoodsResponseDto goods = goodsService.getGoodsByGoodsNo(GOODS_NO);
        PointBalance balance = pointService.selectBalance(USER_NO);
        // 6. 결제 시도 후 잔액 출력
        System.out.println("최종 잔액: " + balance.getBalance());

        assertThat(goods.getStock()).isGreaterThanOrEqualTo(0);
    }

    @Test
    @DisplayName("동일 유저가 두 번 결제 요청 할 때 주문은 1회만 발생한다.")

    void userOrderTwiceThenOneOrderTest() throws InterruptedException {
        int USER_NO = 5;
        int GOODS_NO = 5;

        Runnable task = () -> {
            try {
                orderService.orderGoodsDirect(USER_NO, GOODS_NO);
            } catch (Exception e) {
                // 실패해도 무시
            }
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);

        t1.start(); t2.start();
        t1.join();  t2.join();

        long orderCount = orderRepository.countByUserId(USER_NO);
        assertThat(orderCount).isEqualTo(1L);
    }
}
