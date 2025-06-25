package com.ednaldoluiz.websocket.domain.model.chat;

import java.io.Serializable;

public record ChatSummaryId(
    
    Long userId, 
    Long chatId
    
) implements Serializable {}