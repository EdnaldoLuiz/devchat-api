package com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response;

import java.util.List;

import com.ednaldoluiz.websocket.domain.model.user.Token;

public record LoginResponse(
    String email,
    String name,
    String avatar,
    List<AuthTokenResponse> tokens
) {
    public static LoginResponse from(String email, String name, String avatar, String token, String refreshToken) {
        return new LoginResponse(
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