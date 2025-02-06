package com.ednaldoluiz.websocket.domain.model.user;

import lombok.Getter;

@Getter
public enum Token {
    
    ACCESS("access"),
    REFRESH("refresh");

    private final String token;

    Token(String token) {
        this.token = token;
    }
}
