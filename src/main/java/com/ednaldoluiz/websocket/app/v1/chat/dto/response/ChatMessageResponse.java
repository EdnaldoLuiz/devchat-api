package com.ednaldoluiz.websocket.app.v1.chat.dto.response;

import java.util.Optional;

import com.ednaldoluiz.websocket.domain.model.message.Message;
import com.ednaldoluiz.websocket.domain.model.message.MessageAttachmentType;
import com.ednaldoluiz.websocket.domain.model.message.MessageAttachments;
import com.ednaldoluiz.websocket.domain.model.message.MessageText;

public record ChatMessageResponse(
    Long id,
    String chatId,
    String messageUuid,
    String senderEmail,
    String recipientEmail,
    String content,
    Optional<MessageAttachmentType> type,
    long timestamp
) {

    public static ChatMessageResponse from(
        Message message,
        MessageText text,
        String recipientEmail,
        Optional<MessageAttachments> maybeAttachment
    ) {
        return new ChatMessageResponse(
            message.getId(),
            String.valueOf(message.getChat().getId()),
            message.getMessageUuid().toString(),
            message.getUser().getEmail(),
            recipientEmail,
            text.getContent(),
            maybeAttachment.map(MessageAttachments::getAttachmentType),
            message.getSentAt().toInstant(java.time.ZoneOffset.UTC).toEpochMilli()
        );
    }

    public static ChatMessageResponse from(Message message, MessageText text, String recipientEmail) {
        return from(message, text, recipientEmail, Optional.empty());
    }
}
