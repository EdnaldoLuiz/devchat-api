package com.ednaldoluiz.websocket.web.controller.v1.chat;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TypingEvent {
    private String fromEmail;
    private String toEmail;
    // + getters/setters ou record
}
