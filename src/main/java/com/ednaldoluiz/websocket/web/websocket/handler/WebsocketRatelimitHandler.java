package com.ednaldoluiz.websocket.web.websocket.handler;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.micrometer.core.instrument.Metrics;
import org.springframework.lang.NonNull;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.WebSocketHandlerDecorator;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import java.time.Duration;

public class WebsocketRatelimitHandler implements WebSocketHandlerDecoratorFactory {

    private static final int MAX_MSGS_PER_SEC = 20;

    @Override
    @NonNull
    public WebSocketHandler decorate(@NonNull WebSocketHandler handler) {
        return new WebSocketHandlerDecorator(handler) {

            private final RateLimiter limiter = RateLimiter.of(
                    "ws", RateLimiterConfig.custom()
                            .limitRefreshPeriod(Duration.ofSeconds(1))
                            .limitForPeriod(MAX_MSGS_PER_SEC)
                            .timeoutDuration(Duration.ZERO)
                            .build());

            @Override
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message)
                    throws Exception {
                boolean ok = limiter.acquirePermission();
                if (!ok) {
                    session.close(CloseStatus.POLICY_VIOLATION);
                    return;
                }
                Metrics.counter("ws.messages.in", "user", session.getPrincipal().getName()).increment();
                super.handleMessage(session, message);
            }

            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                Metrics.counter("ws.connections").increment();
                super.afterConnectionEstablished(session);
            }

            @Override
            public void afterConnectionClosed(WebSocketSession s, CloseStatus c) throws Exception {
                Metrics.counter("ws.connections.closed").increment();
                super.afterConnectionClosed(s, c);
            }
        };
    }
}