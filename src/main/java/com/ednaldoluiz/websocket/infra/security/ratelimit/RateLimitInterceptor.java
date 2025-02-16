package com.ednaldoluiz.websocket.infra.security.ratelimit;

import com.ednaldoluiz.websocket.infra.web.handler.exception.RateLimitException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final String HEADER_REMAINING = "X-Rate-Limit-Remaining";

    private final RedisRateLimitService redisRateLimitService;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) {

        String path = request.getRequestURI();
        String clientIP = getClientIP(request);

        // Tenta consumir 1 request
        long remaining;
        try {
            remaining = redisRateLimitService.checkRateLimitAndIncrement(path, clientIP);
        } catch (RateLimitException e) {
            // Lança de novo para seu GlobalExceptionHandler capturar
            throw e;
        }

        // Se chegamos aqui, ainda tem requisições no intervalo
        response.addHeader(HEADER_REMAINING, String.valueOf(remaining));
        return true;
    }

    /**
     * Obtém o IP do cliente, considerando X-Forwarded-For se disponível.
     */
    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null || xfHeader.isEmpty()) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0].trim();
    }
}
