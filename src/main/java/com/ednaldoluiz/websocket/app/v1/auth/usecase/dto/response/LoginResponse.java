package com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response;

public record LoginResponse(

    String token,
    String refreshToken
    
) {}
