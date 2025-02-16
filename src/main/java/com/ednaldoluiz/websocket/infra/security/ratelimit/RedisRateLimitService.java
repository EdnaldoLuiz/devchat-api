package com.ednaldoluiz.websocket.infra.security.ratelimit;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.ednaldoluiz.websocket.infra.web.handler.exception.RateLimitException;

import lombok.RequiredArgsConstructor;

/**
 * Implementa a lógica de rate limit usando Redis puro (Fixed Window).
 * - Cada rota tem uma capacidade (requests) e um interval (minutos).
 * - Armazena a contagem de requests em Redis com TTL = interval.
 */
@Service
@RequiredArgsConstructor
public class RedisRateLimitService {

    private final StringRedisTemplate redisTemplate;
    private final RateLimitProperties rateLimitProperties;

    /**
     * Aplica o rate limit (1 request) para o IP/rota.
     * Retorna quantas requisições RESTAM, ou lança RateLimitException se estourou.
     *
     * @param path Rota acessada
     * @param clientIP IP do cliente
     * @return Número de requisições restantes no intervalo
     */
    public long checkRateLimitAndIncrement(String path, String clientIP) {
        // 1) Busca a regra específica (ou a default)
        RateLimitRule rule = matchRule(path);

        // 2) Monta a chave no Redis
        String key = "rate-limit:" + clientIP + ":" + path;

        // 3) Lê a contagem atual
        String currentValue = redisTemplate.opsForValue().get(key);
        long used = (currentValue != null) ? Long.parseLong(currentValue) : 0;

        // 4) Verifica se ainda está dentro do limite
        if (used >= rule.capacity()) {
            // Já estourou
            long ttl = getTimeToResetSeconds(key);
            // Lança exception com o tempo que falta
            throw new RateLimitException(
                "Muitas requisições para " + path + " (limite: " + rule.capacity() + ")",
                ttl,
                path
            );
        }

        // 5) Ainda não estourou: incrementa
        used++;
        redisTemplate.opsForValue().set(key, String.valueOf(used));

        // 6) Se for a 1ª vez, define TTL = refillInterval
        if (used == 1) {
            redisTemplate.expire(key, Duration.ofMinutes(rule.refillInterval()));
        }

        // 7) Calcula quantos restam
        long remaining = rule.capacity() - used;

        return remaining;
    }

    /**
     * Retorna o tempo (em segundos) até o Redis expirar essa chave (ou 0 se não existir).
     */
    private long getTimeToResetSeconds(String key) {
        Long expire = redisTemplate.getExpire(key);
        if (expire == null || expire < 0) {
            return 0; // Significa que não tem TTL ou não existe
        }
        return expire;
    }

    /**
     * Procura a regra correspondente à rota ou retorna a default.
     */
    private RateLimitRule matchRule(String path) {
        Optional<RateLimitRule> matched = rateLimitProperties.getRoutes().stream()
                .filter(r -> path.equals(r.path()))
                .findFirst();
        return matched.orElse(rateLimitProperties.getDefaultRule());
    }

}
