package com.ednaldoluiz.websocket.web.websocket.strategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.Optional;

@Slf4j
@Component
public class SubscribeCommandStrategy implements StompCommandStrategy {

    @Override
    public void handle(StompHeaderAccessor accessor) {
        String principal = Optional.ofNullable(accessor.getUser())
                .map(Principal::getName)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        log.info("Subscribed to user {}", principal);

        Optional.ofNullable(accessor.getDestination())
                .filter(dest -> dest.startsWith("/user/") &&
                        !dest.equals("/user/queue/messages") &&
                        !dest.contains(principal))
                .ifPresent(dest -> {
                    throw new IllegalArgumentException("Forbidden subscription: " + dest);
                });
    }
}