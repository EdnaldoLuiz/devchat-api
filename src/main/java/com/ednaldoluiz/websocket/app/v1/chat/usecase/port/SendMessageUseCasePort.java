package com.ednaldoluiz.websocket.app.v1.chat.usecase.port;

public interface SendMessageUseCasePort {

    void sendMessage(Long chatId, Long senderId, String content);
    
}