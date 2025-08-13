package com.ednaldoluiz.websocket.app.v1.chat.dto.response;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import com.ednaldoluiz.websocket.domain.model.message.Message;
import com.ednaldoluiz.websocket.domain.model.message.MessageAttachmentType;
import com.ednaldoluiz.websocket.domain.model.message.MessageAttachments;
import com.ednaldoluiz.websocket.domain.model.message.CipherType; // <- importe seu enum

public record ChatMessageResponse(
        Long id,
        UUID messageUuid,
        Long senderId,
        Long recipientId,
        CipherPayload content,
        MessageAttachmentType attachmentType,
        LocalDateTime timestamp
) {
    /** Construtor extra usado pelo JPQL: recebe o enum + bytes e monta o CipherPayload. */
    public ChatMessageResponse(
            Long id,
            UUID messageUuid,
            Long senderId,
            Long recipientId,
            CipherType cipherType,           // vem do m.cipherType
            byte[] cipherBody,               // vem do m.cipherBody
            MessageAttachmentType attachmentType,
            LocalDateTime timestamp
    ) {
        this(
            id,
            messageUuid,
            senderId,
            recipientId,
            new CipherPayload(
                cipherType.getCode(),        // usa o código numérico do enum
                Base64.getEncoder().encodeToString(cipherBody) // body em Base64 p/ o front
            ),
            attachmentType,
            timestamp
        );
    }

    /* ---------- factory p/ mensagem já no banco ---------- */
    public static ChatMessageResponse from(
            Message msg,
            Long recipientId,
            Optional<MessageAttachments> maybeAttach
    ) {
        return new ChatMessageResponse(
                msg.getId(),
                msg.getMessageUuid(),
                msg.getUser().getId(),
                recipientId,
                msg.getCipherType(),
                msg.getCipherBody(),
                maybeAttach.map(MessageAttachments::getAttachmentType).orElse(null),
                msg.getSentAt()
        );
    }

    /* ---------- factory p/ buffer Redis (sem DB) ---------- */
    public static ChatMessageResponse from(BufferedMessage buf, Long recipientId, boolean _fallback) {
        return new ChatMessageResponse(
                null,
                buf.messageUuid(),
                buf.fromId(),
                recipientId,
                new CipherPayload(buf.cipherType(), buf.cipherBodyB64()),
                buf.hasAttachment() ? buf.attachmentType() : null,
                java.time.Instant.ofEpochMilli(buf.sentAtMillis())
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDateTime()
        );
    }
}
