package com.ednaldoluiz.websocket.app.v1.chat.usecase.port;

public interface StartPrivateChatUseCasePort {

    Long startPrivateChat(Long userId1, Long userId2);
    
}