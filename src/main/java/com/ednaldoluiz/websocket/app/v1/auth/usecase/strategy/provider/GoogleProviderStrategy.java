package com.ednaldoluiz.websocket.app.v1.auth.usecase.strategy.provider;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.OAuth2UserInfoRequest;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.strategy.OAuth2ProviderStrategy;

@Component
public class GoogleProviderStrategy implements OAuth2ProviderStrategy {

    @Override
    public void validateAttributes(Map<String, Object> attributes) {
        if (!attributes.containsKey("email") || attributes.get("email") == null) {
            throw new IllegalArgumentException("Google OAuth2: Email é obrigatório.");
        }
        if (!attributes.containsKey("name") || attributes.get("name") == null) {
            throw new IllegalArgumentException("Google OAuth2: Nome é obrigatório.");
        }
    }

    @Override
    public OAuth2UserInfoRequest extractUserInfo(Map<String, Object> attributes) {
        return OAuth2UserInfoRequest.builder()
            .email((String) attributes.get("email"))
            .name((String) attributes.get("name"))
            .avatar((String) attributes.getOrDefault("picture", null))
            .build();
    }
}