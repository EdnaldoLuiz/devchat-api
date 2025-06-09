package com.ednaldoluiz.websocket.app.v1.chat.dto.response;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import com.ednaldoluiz.websocket.domain.model.message.Message;
import com.ednaldoluiz.websocket.domain.model.message.MessageAttachmentType;
import com.ednaldoluiz.websocket.domain.model.message.MessageAttachments;
import com.ednaldoluiz.websocket.domain.model.message.MessageText;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public record ChatMessageResponse(

        @JsonSerialize(using = ToStringSerializer.class) 
        Long id,

        @JsonSerialize(using = ToStringSerializer.class) // <-- vai sair como string no JSON
        UUID messageUuid,

        @JsonSerialize(using = ToStringSerializer.class) 
        Long senderId,

        @JsonSerialize(using = ToStringSerializer.class) 
        Long recipientId,

        String content,
        MessageAttachmentType type,
        LocalDateTime timestamp) {

    public static ChatMessageResponse from(
            Message message,
            MessageText text,
            Long recipientId,
            Optional<MessageAttachments> maybeAttachment) {
        return new ChatMessageResponse(
                message.getId(),
                message.getMessageUuid(),
                message.getUser().getId(),
                recipientId,
                text.getContent(),
                maybeAttachment.map(MessageAttachments::getAttachmentType).orElse(null),
                message.getSentAt());
    }

    public static ChatMessageResponse from(Message message, MessageText messageText, Long r) {
        return from(message, messageText, r, Optional.empty());
    }
}
