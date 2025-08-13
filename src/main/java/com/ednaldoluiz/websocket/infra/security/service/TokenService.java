package com.ednaldoluiz.websocket.infra.security.service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final StringRedisTemplate redis;

    private static final String REDIS_BLACKLIST_PREFIX = "blacklisted:";

    /**
     * Invalida um token colocando seu jti na blacklist com um TTL igual ao tempo de expiração restante.
     */
    public void invalidateToken(String jti, Date expiration) {
        long ttlMillis = expiration.getTime() - System.currentTimeMillis();
        if (ttlMillis > 0) {
            long ttlSeconds = TimeUnit.MILLISECONDS.toSeconds(ttlMillis);
            redis.opsForValue().set(REDIS_BLACKLIST_PREFIX + jti, "BLACKLISTED", ttlSeconds, TimeUnit.SECONDS);
        }
    }

    /**
     * Verifica se o token está na blacklist.
     */
    public boolean isTokenBlacklisted(String jti) {
        return Boolean.TRUE.equals(redis.hasKey(REDIS_BLACKLIST_PREFIX + jti));
    }
}