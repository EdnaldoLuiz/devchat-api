package com.ednaldoluiz.websocket.app.v1.user.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public record UserSearchResponse(
        @JsonSerialize(using = ToStringSerializer.class)
        Long id,
        String name,
        String avatar,
        String email
) {}
