package com.example.api.service;

import com.example.api.repository.CouponRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ApplyServiceImplTest {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    public void 한번만_응모() {
        applyService.apply(1L);

        long count = couponRepository.count();
        System.out.println("count : "+ count);

        assertThat(count).isEqualTo(1);
    }

    @Test
    public void 여러명_응모() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            long userId = i;
            System.out.println("user id : " + userId);
            executorService.submit(() -> {
                try {
                    applyService.apply(userId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Thread.sleep(1000);

        long count = couponRepository.count();
        System.out.println("count : " + count);

        assertThat(count).isEqualTo(100);
    }

    @Test
    public void 인당_한개의_쿠폰_발급() throws InterruptedException {
        int userCount = 1;
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            long userId = 1;
            System.out.println("user id : " + userId);
            executorService.submit(() -> {
                try {
                    applyService.apply(userId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Thread.sleep(1000);

        long count = couponRepository.count();
        System.out.println("count : " + count);

        assertThat(count).isEqualTo(userCount);
    }
}