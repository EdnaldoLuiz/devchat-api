package com.ednaldoluiz.websocket.app.v1.chat.dto.request;

import java.util.UUID;

public record SendTextMessageRequest(

        UUID messageUuid,
        String toEmail,
        String content

) { }