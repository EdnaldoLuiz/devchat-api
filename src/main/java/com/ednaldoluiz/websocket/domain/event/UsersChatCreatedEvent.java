package com.ednaldoluiz.websocket.domain.event;

public record UsersChatCreatedEvent(Long chatId, Long userId) {}