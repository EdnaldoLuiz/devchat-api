package com.ednaldoluiz.websocket.app.v1.auth.dto.response;

public record LogoutResponse(String message) {

    public static LogoutResponse success() {
        return new LogoutResponse("Logout realizado com sucesso.");
    }
}
