package com.ednaldoluiz.websocket.app.v1.chat.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

import com.ednaldoluiz.websocket.domain.model.message.MessageAttachmentType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public record ChatRealtimeEnvelopeResponse(
        Long id,
        UUID messageUuid,
        @JsonSerialize(using = ToStringSerializer.class) Long senderId,
        @JsonSerialize(using = ToStringSerializer.class) Long recipientId,
        CipherPayload content,
        MessageAttachmentType attachmentType,
        LocalDateTime timestamp
    ) {

    public static ChatRealtimeEnvelopeResponse fromRaw(
            Long messageId, UUID messageUuid,
            Long senderId, Long recipientId,
            int cipherNumType, byte[] cipherBody,
            MessageAttachmentType attType, LocalDateTime ts
    ) {
        String b64 = java.util.Base64.getEncoder().encodeToString(cipherBody);
        return new ChatRealtimeEnvelopeResponse(
                messageId, messageUuid, senderId, recipientId,
                new CipherPayload(cipherNumType, b64),
                attType, ts);
    }

    public String getContentType() {
        return (attachmentType == null) ? "TEXT" : attachmentType.name();
    }
}
