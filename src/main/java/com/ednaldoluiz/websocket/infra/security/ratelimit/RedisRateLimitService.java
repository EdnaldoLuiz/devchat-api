package com.ednaldoluiz.websocket.infra.security.ratelimit;

import java.time.Duration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.ednaldoluiz.websocket.infra.web.handler.exception.RateLimitException;

import lombok.RequiredArgsConstructor;

/**
 * Exemplo de Rate Limit usando Redis manualmente, com bloqueio exponencial.
 */
@Service
@RequiredArgsConstructor
public class RedisRateLimitService {

    private final StringRedisTemplate redisTemplate;
    private final RateLimitRouter rateLimitRouter;

    /**
     * Verifica se o IP pode fazer +1 requisição na rota. 
     * Se ultrapassar o limite, lança exceção com o tempo em segundos p/ liberar.
     * 
     * @return quantas requisições RESTAM.
     */
    public long checkRateLimitAndIncrement(String path, String clientIP) {
        RateLimitPolicy policy = rateLimitRouter.resolvePolicy(path);
        
        String countKey = buildCountKey(clientIP, path);
        String blockKey = buildBlockKey(clientIP, path);

        long used = getLong(countKey, 0L);
        long blockCount = getLong(blockKey, 0L);

        if (used >= policy.capacity()) {
            return handleBlocked(path, policy, countKey, blockKey, used, blockCount);
        }

        // 5) Se não excedeu, incrementa contagem
        used += 1;
        redisTemplate.opsForValue().set(countKey, String.valueOf(used));

        // Se for a 1ª vez nessa janela (e não estava bloqueado),
        // define a TTL = refillInterval (min) pro countKey
        if (used == 1 && blockCount == 0) {
            redisTemplate.expire(countKey, Duration.ofMinutes(policy.refillInterval()));
        }

        return policy.capacity() - used;
    }

    /**
     * Trata a situação em que o usuário já atingiu ou excedeu a capacidade.
     */
    private long handleBlocked(String path, RateLimitPolicy policy,
            String countKey, String blockKey,
            long used, long blockCount) {

        // Se ainda tiver TTL no blockKey, significa que continua bloqueado
        Long blockTTL = redisTemplate.getExpire(blockKey);
        if (blockTTL != null && blockTTL > 0) {
            throw new RateLimitException(
                "Muitas requisições para %s (limite: %d). Tente novamente em %d segundos."
                    .formatted(path, policy.capacity(), blockTTL),
                blockTTL,
                path
            );
        }

        // Caso contrário, inicia um novo ciclo de bloqueio
        blockCount += 1;
        long blockSeconds = computeBlockSeconds(policy.refillInterval(), blockCount);

        // Atualiza Redis com as infos
        redisTemplate.opsForValue().set(blockKey, String.valueOf(blockCount));
        redisTemplate.opsForValue().set(countKey, String.valueOf(used));

        // Define TTL = blockSeconds para ambos
        redisTemplate.expire(blockKey, Duration.ofSeconds(blockSeconds));
        redisTemplate.expire(countKey, Duration.ofSeconds(blockSeconds));

        throw new RateLimitException(
            "Muitas requisições para %s (limite: %d). Tente novamente em %d segundos."
                .formatted(path, policy.capacity(), blockSeconds),
            blockSeconds,
            path
        );
    }

    /**
     * Faz o cálculo do tempo de bloqueio exponencial, por ex: 2^(blockCount-1) * refillInterval * 60
     */
    private long computeBlockSeconds(int refillIntervalMinutes, long blockCount) {
        long baseSeconds = refillIntervalMinutes * 60L;
        return (long) (Math.pow(2, blockCount - 1) * baseSeconds);
    }

    private long getLong(String redisKey, long defaultValue) {
        String val = redisTemplate.opsForValue().get(redisKey);
        if (val == null) return defaultValue;
        return Long.parseLong(val);
    }

    private String buildCountKey(String ip, String path) {
        return "rl:" + ip + ":" + path + ":count";
    }

    private String buildBlockKey(String ip, String path) {
        return "rl:" + ip + ":" + path + ":block";
    }
}