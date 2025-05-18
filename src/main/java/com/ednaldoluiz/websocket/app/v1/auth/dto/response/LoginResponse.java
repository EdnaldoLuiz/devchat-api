package com.ednaldoluiz.websocket.app.v1.auth.dto.response;

import java.util.List;

import com.ednaldoluiz.websocket.domain.model.user.Token;

public record LoginResponse(

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