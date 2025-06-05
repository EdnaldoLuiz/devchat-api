package com.ednaldoluiz.websocket.app.v1.chat.dto.response;

import java.util.Optional;

import com.ednaldoluiz.websocket.domain.model.message.Message;
import com.ednaldoluiz.websocket.domain.model.message.MessageAttachmentType;
import com.ednaldoluiz.websocket.domain.model.message.MessageAttachments;
import com.ednaldoluiz.websocket.domain.model.message.MessageText;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public record ChatMessageResponse(

    @JsonSerialize(using = ToStringSerializer.class)
    Long id,
    
    String chatId,
    String messageUuid,

    @JsonSerialize(using = ToStringSerializer.class)
    Long senderId,

    @JsonSerialize(using = ToStringSerializer.class)
    Long recipientId,

    String content,
    Optional<MessageAttachmentType> type,
    long timestamp
) {

    public static ChatMessageResponse from(
        Message message,
        MessageText text,
        Long recipientId,
        Optional<MessageAttachments> maybeAttachment
    ) {
        return new ChatMessageResponse(
            message.getId(),
            String.valueOf(message.getChat().getId()),
            message.getMessageUuid().toString(),
            message.getUser().getId(),
            recipientId,
            text.getContent(),
            maybeAttachment.map(MessageAttachments::getAttachmentType),
            message.getSentAt().toInstant(java.time.ZoneOffset.UTC).toEpochMilli()
        );
    }

    public static ChatMessageResponse from(Message message, MessageText text, Long recipientId) {
        return from(message, text, recipientId, Optional.empty());
    }
}
