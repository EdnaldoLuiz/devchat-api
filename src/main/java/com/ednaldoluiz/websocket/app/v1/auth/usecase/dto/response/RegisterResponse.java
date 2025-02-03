package com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response;

public record RegisterResponse(
    Long userId,
    String email,
    String token,
    String refreshToken
) {
    public static RegisterResponse from(Long userId, String email, String token, String refreshToken) {
        return new RegisterResponse(
            userId,
            email,
            token,
            refreshToken
        );
    }
}