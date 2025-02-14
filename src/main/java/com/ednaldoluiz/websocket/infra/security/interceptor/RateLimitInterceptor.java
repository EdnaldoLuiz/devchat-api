package com.ednaldoluiz.websocket.infra.security.interceptor;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import io.github.bucket4j.local.SynchronizationStrategy;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    // Exemplo de 100 requisições por minuto
    private static final int REQUESTS_PER_MINUTE = 100;
    private static final Duration REFILL_INTERVAL = Duration.ofMinutes(1);

    // Em um ambiente distribuído, este mapa deve ser substituído por uma solução de cache compartilhado (por exemplo, Redis)
    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    /**
     * Cria a regra de limite de requisições
     */
    private Bandwidth createBandwidthLimit() {
        return Bandwidth.builder()
            .capacity(REQUESTS_PER_MINUTE)
            .refillGreedy(REQUESTS_PER_MINUTE, REFILL_INTERVAL)
            .initialTokens(REQUESTS_PER_MINUTE)
            .build();
    }

    /**
     * Cria um novo bucket usando a configuração de banda definida
     */
    private Bucket createNewBucket() {
        return Bucket.builder()
                .addLimit(createBandwidthLimit())
                .withSynchronizationStrategy(SynchronizationStrategy.SYNCHRONIZED)
                .withNanosecondPrecision()
                .build();
    }

    /**
     * Rate limiting antes de cada requisição
     */
    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws Exception {

        // Escolha da "chave": por IP ou qualquer identificação, como API Key, se houver
        String clientKey = getClientIP(request);

        Bucket bucket = buckets.computeIfAbsent(clientKey, k -> createNewBucket());
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            response.addHeader("X-Rate-Limit-Remaining", String.valueOf(probe.getRemainingTokens()));
            return true;
        }
        long waitForRefillSeconds = probe.getNanosToWaitForRefill() / 1_000_000_000;
        response.addHeader("X-Rate-Limit-Retry-After-Seconds", String.valueOf(waitForRefillSeconds));
        response.sendError(HttpStatus.TOO_MANY_REQUESTS.value(), "Too Many Requests");
        return false;
    }

    /**
     * Captura o IP do cliente (ou o primeiro IP em caso de X-Forwarded-For)
     */
    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0].trim();
    }
}