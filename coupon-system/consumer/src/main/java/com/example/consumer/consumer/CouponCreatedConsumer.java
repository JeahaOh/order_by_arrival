package com.example.consumer.consumer;

import com.example.consumer.domain.Coupon;
import com.example.consumer.domain.FailedIssueEvent;
import com.example.consumer.repository.CouponRepository;
import com.example.consumer.repository.FailedIssueEventRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class CouponCreatedConsumer {

    private final CouponRepository couponRepository;

    private final FailedIssueEventRepository failedIssueEventRepository;

    @KafkaListener(topics = "coupon_create", groupId = "group_1")
    public void listener(Long userId) {
        System.out.println("userId : " + userId);
        try {
            couponRepository.save(new Coupon(userId));
        } catch (Exception e) {
            log.error("CUZ : {}, MSG : {}", e.getCause(), e.getMessage());
            failedIssueEventRepository.save(new FailedIssueEvent(userId));
        }
    }
}
