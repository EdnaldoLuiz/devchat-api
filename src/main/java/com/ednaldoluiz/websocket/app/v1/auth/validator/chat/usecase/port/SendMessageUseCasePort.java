package com.ednaldoluiz.websocket.app.v1.auth.validator.chat.usecase.port;

public interface SendMessageUseCasePort {

    void sendMessage(Long chatId, Long senderId, String content);
    
}