package ru.pozhar.collector_api.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class NotificationCacheService {
    private final StringRedisTemplate redisTemplate;

    public void cacheNotification(Long debtorId, Long agreementId, String notification) {
        String key = generateKey(debtorId, agreementId);
        redisTemplate.opsForValue().set(key, notification, 60, TimeUnit.DAYS);
    }

    public String getNotification(Long debtorId, Long agreementId) {
        String key = generateKey(debtorId, agreementId);
        return redisTemplate.opsForValue().get(key);
    }

    public boolean hasNotification(Long debtorId, Long agreementId) {
        String key = generateKey(debtorId, agreementId);
        return redisTemplate.hasKey(key);
    }

    private String generateKey(Long debtorId, Long agreementId) {
        return debtorId + "_" + agreementId;
    }
}
