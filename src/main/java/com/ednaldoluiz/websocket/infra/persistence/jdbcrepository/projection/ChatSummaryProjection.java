package com.ednaldoluiz.websocket.infra.persistence.jdbcrepository.projection;

import java.time.LocalDateTime;

public record ChatSummaryProjection(
    Long chatId,
    Long participantId,
    Long roomId,
    Long lastMessageId,
    Long lastMessageSenderId,
    String participantName,
    String participantAvatar,
    LocalDateTime lastMessageAt,
    int unreadCount
) {}
