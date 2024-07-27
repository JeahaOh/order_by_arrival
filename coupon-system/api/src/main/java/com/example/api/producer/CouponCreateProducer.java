package com.example.api.producer;

import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CouponCreateProducer {

    private final KafkaTemplate<String, Long> kafkaTemplate;

    public void create(Long userId) {
        kafkaTemplate.send("coupon_create", userId);
    }
}
