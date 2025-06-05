package com.ednaldoluiz.websocket.app.v1.chat.command;

import java.util.UUID;

public record TextMessageCommand (

    UUID   messageUuid,
    Long toUserId,
    String content
    
) implements SendMessageCommand {}
