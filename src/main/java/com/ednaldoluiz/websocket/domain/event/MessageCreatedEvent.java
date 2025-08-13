package com.ednaldoluiz.websocket.domain.event;

import java.time.LocalDateTime;

public record MessageCreatedEvent(
    Long messageId, 
    Long chatId, 
    Long senderId, 
    LocalDateTime sentAt, 
    String content
) {}
