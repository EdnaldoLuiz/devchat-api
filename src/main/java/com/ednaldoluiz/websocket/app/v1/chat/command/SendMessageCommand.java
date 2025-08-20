package com.ednaldoluiz.websocket.app.v1.chat.command;

import java.util.UUID;

import com.ednaldoluiz.websocket.app.v1.chat.dto.response.CipherPayload;
import com.ednaldoluiz.websocket.app.v1.chat.dto.response.HistoryPayload;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextMessageCommand.class, name = "TEXT"),
        @JsonSubTypes.Type(value = AttachmentMessageCommand.class, name = "ATTACHMENT")
})
public sealed interface SendMessageCommand permits TextMessageCommand, AttachmentMessageCommand {

    UUID messageUuid();

    Long toUserId();

    CipherPayload content();

    CipherPayload senderCopy();

    HistoryPayload history();

}