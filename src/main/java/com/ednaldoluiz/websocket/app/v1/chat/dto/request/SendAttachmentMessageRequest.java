package com.ednaldoluiz.websocket.app.v1.chat.dto.request;

import java.util.UUID;

import com.ednaldoluiz.websocket.domain.model.message.MessageAttachmentType;

public record SendAttachmentMessageRequest(

        UUID               messageUuid,
        String               toEmail,
        String               content,
        MessageAttachmentType attachmentType,
        String               url

) { }
