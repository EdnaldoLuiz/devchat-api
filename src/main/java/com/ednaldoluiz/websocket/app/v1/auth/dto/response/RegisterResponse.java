package com.ednaldoluiz.websocket.app.v1.auth.dto.response;

import java.util.List;

import com.ednaldoluiz.websocket.domain.model.user.Token;

public record RegisterResponse(
    Long userId,
    String email,
    List<AuthTokenResponse> tokens
) {
    public static RegisterResponse from(Long userId, String email, String token, String refreshToken) {
        return new RegisterResponse(
            userId,
            email,
            List.of(
                AuthTokenResponse.of(Token.ACCESS, token),
                AuthTokenResponse.of(Token.REFRESH, refreshToken)
            )
        );
    }
}