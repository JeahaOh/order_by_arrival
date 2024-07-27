package com.example.api.repository;

import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class AppliedUserRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public Long add(Long userId) {
        return redisTemplate
                .opsForSet()
                .add("applied_user", userId.toString());
    }
}
