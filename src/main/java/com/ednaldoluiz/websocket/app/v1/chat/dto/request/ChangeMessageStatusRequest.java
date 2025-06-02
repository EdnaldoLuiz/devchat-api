package com.ednaldoluiz.websocket.app.v1.chat.dto.request;

import com.ednaldoluiz.websocket.domain.model.message.MessageStatusType;

public record ChangeMessageStatusRequest(

    Long              messageId,
    MessageStatusType newStatus
    
) {}
