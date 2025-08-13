package com.ednaldoluiz.websocket.domain.event;

import com.ednaldoluiz.websocket.domain.model.message.MessageStatusType;

public record MessageStatusChangedEvent(
    Long messageId, 
    Long chatId, 
    Long userId, 
    MessageStatusType oldStatus, 
    MessageStatusType newStatus
) {}