package com.ednaldoluiz.websocket.app.v1.chat.command;

import com.ednaldoluiz.websocket.app.v1.chat.dto.response.CipherPayload;

import java.util.UUID;

public record TextMessageCommand (

    UUID   messageUuid,
    Long toUserId,
    CipherPayload content

) implements SendMessageCommand {}
