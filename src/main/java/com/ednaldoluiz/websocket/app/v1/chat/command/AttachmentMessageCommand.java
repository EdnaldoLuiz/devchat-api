package com.ednaldoluiz.websocket.app.v1.chat.command;

import java.util.UUID;

import com.ednaldoluiz.websocket.app.v1.chat.dto.response.CipherPayload;
import com.ednaldoluiz.websocket.domain.model.message.MessageAttachmentType;

public record AttachmentMessageCommand (

    UUID                  messageUuid,
    Long                  toUserId,
    CipherPayload content,
    MessageAttachmentType type,
    String                url

) implements SendMessageCommand {}
