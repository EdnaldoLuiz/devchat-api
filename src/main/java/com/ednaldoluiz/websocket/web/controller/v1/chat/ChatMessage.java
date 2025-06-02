package com.ednaldoluiz.websocket.web.controller.v1.chat;

import java.util.UUID;

public record ChatMessage(
        String id,
        String chatId,
        UUID messageUuid,
        String senderEmail,
        String recipientEmail,
        String content,
        String type,
        long timestamp
) {

}
