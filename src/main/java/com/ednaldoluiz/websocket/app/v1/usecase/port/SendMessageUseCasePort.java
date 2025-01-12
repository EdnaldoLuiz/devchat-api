package com.ednaldoluiz.websocket.app.v1.usecase.port;

import com.ednaldoluiz.websocket.app.v1.usecase.dto.request.SendMessageRequest;
import com.ednaldoluiz.websocket.app.v1.usecase.dto.response.SendMessageResponse;

public interface SendMessageUseCasePort {

    SendMessageResponse sendMessage(SendMessageRequest request);
    
}
