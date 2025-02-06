package com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response;

public record LogoutResponse(String message) {

    public static LogoutResponse success() {
        return new LogoutResponse("Logout realizado com sucesso.");
    }
}
