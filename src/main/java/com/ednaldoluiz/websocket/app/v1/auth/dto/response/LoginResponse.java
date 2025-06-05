package com.ednaldoluiz.websocket.app.v1.auth.dto.response;

import java.util.List;

import com.ednaldoluiz.websocket.domain.model.user.Token;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

public record LoginResponse(

        @JsonSerialize(using = ToStringSerializer.class)
        Long id,
        String email,
        String name,
        String avatar,
        List<AuthTokenResponse> tokens
) {
    public static LoginResponse from(Long id, String email, String name, String avatar, String token, String refreshToken) {
        return new LoginResponse(
                id,
            email,
            name,
            avatar,
            List.of(
                AuthTokenResponse.of(Token.ACCESS, token),
                AuthTokenResponse.of(Token.REFRESH, refreshToken)
            )
        );
    }
}