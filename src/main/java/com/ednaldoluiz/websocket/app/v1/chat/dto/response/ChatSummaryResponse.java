package com.ednaldoluiz.websocket.app.v1.chat.dto.response;

import java.time.LocalDateTime;

import com.ednaldoluiz.websocket.infra.persistence.jdbcrepository.projection.ChatSummaryProjection;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ChatSummaryResponse(

    @JsonSerialize(using = ToStringSerializer.class)
    Long chatId,

    @JsonSerialize(using = ToStringSerializer.class)
    Long participantId,

    @JsonSerialize(using = ToStringSerializer.class)
    Long roomId,

    @JsonSerialize(using = ToStringSerializer.class)
    Long lastMessageId,

    @JsonSerialize(using = ToStringSerializer.class)
    Long lastMessageSenderId,

    String participantName,
    String participantAvatar,

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime lastMessageAt,

    int unreadCount
) {
    public static ChatSummaryResponse from(ChatSummaryProjection projection) {
        return new ChatSummaryResponse(
            projection.chatId(),
            projection.participantId(),
            projection.roomId(),
            projection.lastMessageId(),
            projection.lastMessageSenderId(),
            projection.participantName(),
            projection.participantAvatar(),
            projection.lastMessageAt(),
            projection.unreadCount()
        );
    }
}
