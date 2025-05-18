package com.ednaldoluiz.websocket.web.controller.v1.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    private String id;
    private String chatId;
    private UUID messageUuid;
    private String senderEmail;
    private String recipientEmail;
    private String content;
    private String type;   // TEXT, IMAGE, FILE
    private long timestamp;
}
