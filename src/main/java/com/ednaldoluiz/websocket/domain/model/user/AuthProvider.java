package com.ednaldoluiz.websocket.domain.model.user;

public enum AuthProvider {

    EMAIL_PASSWORD,      
    GITHUB,     
    GOOGLE,
    FACEBOOK,
    TWITTER,
    LINKEDIN;

    public static AuthProvider fromString(String provider) {
        return AuthProvider.valueOf(provider.toUpperCase());
    }
}