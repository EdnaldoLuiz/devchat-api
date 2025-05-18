package com.ednaldoluiz.websocket.web.websocket.store;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class OnlineUserStore {

    // userId → set de sessionIds
    private final ConcurrentMap<String, Set<String>> online = new ConcurrentHashMap<>();

    public void add(String userId, String sessionId) {
        online.computeIfAbsent(userId, id -> ConcurrentHashMap.newKeySet())
                .add(sessionId);
    }

    public void remove(String userId, String sessionId) {
        Optional.ofNullable(online.get(userId))
                .ifPresent(sessions -> {
                    sessions.remove(sessionId);
                    if (sessions.isEmpty()) online.remove(userId);
                });
    }

    public Set<String> getSessions(String userId) {
        return online.getOrDefault(userId, Collections.emptySet());
    }
}
