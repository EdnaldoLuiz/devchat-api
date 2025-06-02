package com.ednaldoluiz.websocket.app.v1.chat.command;

import java.util.UUID;

import com.ednaldoluiz.websocket.domain.model.message.MessageAttachmentType;

public record AttachmentMessageCommand (

    UUID                  messageUuid,
    String                toEmail,
    String                content,
    MessageAttachmentType type,
    String                url

) implements SendMessageCommand {}
