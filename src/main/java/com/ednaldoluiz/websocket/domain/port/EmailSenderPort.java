package com.ednaldoluiz.websocket.domain.port;

public interface EmailSenderPort {

    void sendEmail(String from, String to, String subject, String bodyHtml);
    
}