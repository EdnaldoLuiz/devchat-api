package com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response;

import com.ednaldoluiz.websocket.domain.model.user.Token;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AuthTokenResponse(
    Token type,
    String value
) {
    public static AuthTokenResponse of(Token type, String value) {
        return new AuthTokenResponse(type, value);
    }
}