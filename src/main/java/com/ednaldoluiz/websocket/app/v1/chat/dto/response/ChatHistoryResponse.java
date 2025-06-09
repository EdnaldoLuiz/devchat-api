package com.ednaldoluiz.websocket.app.v1.chat.dto.response;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public record ChatHistoryResponse(

    @JsonSerialize(using = ToStringSerializer.class)
    Long chatId,

    int page,
    int size,
    long total,

    List<ChatMessageResponse> messages

) {}