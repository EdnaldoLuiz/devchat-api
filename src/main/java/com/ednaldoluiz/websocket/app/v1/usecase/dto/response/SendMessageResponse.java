package com.ednaldoluiz.websocket.app.v1.usecase.dto.response;

import com.ednaldoluiz.websocket.domain.model.message.Message;

public record SendMessageResponse(

    Long id,
    String message
    
) {

    public static SendMessageResponse from(Message message) {
        return new SendMessageResponse(
            message.getId(),
            message.getMessageText().getContent()
        );
    }
}
