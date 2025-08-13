package com.ednaldoluiz.websocket.web.websocket.event;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class SignalEventPublisher {

    private final SimpMessagingTemplate messaging;

    public void notifyLowPreKey(Long userId, int remaining) {
        messaging.convertAndSendToUser(
            userId.toString(),
            "/queue/signal/events",
            Map.of("type", "LOW_PREKEY", "remaining", remaining)
        );
    }

    public void notifySignedPreKeyExpired(Long userId) {
        messaging.convertAndSendToUser(
            userId.toString(),
            "/queue/signal/events",
            Map.of("type", "SPK_EXPIRED")
        );
    }
}
