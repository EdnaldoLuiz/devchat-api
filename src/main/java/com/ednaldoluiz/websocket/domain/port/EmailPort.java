package com.ednaldoluiz.websocket.domain.port;

public interface EmailPort {

    void sendEmail(String from, String to, String subject, String bodyHtml);
    
}