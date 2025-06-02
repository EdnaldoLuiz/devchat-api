package com.ednaldoluiz.websocket.app.v1.chat.dto.response;

import java.util.List;

public record ChatHistoryResponse(

    Long  chatId,
    int   page,
    int   size,
    long  total,
    List<ChatMessageResponse> messages
    
) {}
