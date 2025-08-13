package com.ednaldoluiz.websocket.app.v1.chat.dto.response;

import java.time.LocalDateTime;

import com.ednaldoluiz.websocket.domain.model.chat.ChatSummary;
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
    public static ChatSummaryResponse from(ChatSummary projection) {
        return new ChatSummaryResponse(
            projection.getChatId(),
            projection.getParticipantId(),
            projection.getRoomId(),
            projection.getLastMessageId(),
            projection.getLastMessageSenderId(),
            projection.getParticipantName(),
            projection.getParticipantAvatar(),
            projection.getLastMessageAt(),
            projection.getUnreadCount()
        );
    }
}
