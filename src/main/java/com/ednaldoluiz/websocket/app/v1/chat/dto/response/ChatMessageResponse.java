// src/main/java/com/ednaldoluiz/websocket/app/v1/chat/dto/response/ChatMessageResponse.java
package com.ednaldoluiz.websocket.app.v1.chat.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.ednaldoluiz.websocket.domain.model.message.MessageAttachmentType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public record ChatMessageResponse(
        Long id,
        UUID messageUuid,

        @JsonSerialize(using = ToStringSerializer.class) 
        Long senderId,
        @JsonSerialize(using = ToStringSerializer.class)
        Long recipientId,

        HistoryPayload history,

        MessageAttachmentType attachmentType,
        LocalDateTime timestamp
) {

    public ChatMessageResponse(
            Long id,
            UUID messageUuid,
            Long senderId,
            Long recipientId,

            String historyAlgorithm,
            Integer historyVersion,
            byte[] historyInitializationVector,
            byte[] historyCiphertext,

            MessageAttachmentType attachmentType,
            LocalDateTime timestamp) {
        this(
            id,
            messageUuid,
            senderId,
            recipientId,
            HistoryPayload.fromRaw(
                historyAlgorithm,
                historyVersion,
                historyInitializationVector,
                historyCiphertext),
            attachmentType,
            timestamp
        );
    }

    public String getContentType() {
        return (attachmentType == null) ? "TEXT" : attachmentType.name();
    }
}
