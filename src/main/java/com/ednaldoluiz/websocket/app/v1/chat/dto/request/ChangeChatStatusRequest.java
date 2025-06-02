package com.ednaldoluiz.websocket.app.v1.chat.dto.request;

import com.ednaldoluiz.websocket.domain.model.chat.ChatStatus;

public record ChangeChatStatusRequest(
    
    Long       chatId,
    ChatStatus newStatus
    
) {}

