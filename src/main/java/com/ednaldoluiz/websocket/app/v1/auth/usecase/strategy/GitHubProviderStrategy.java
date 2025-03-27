package com.ednaldoluiz.websocket.app.v1.auth.usecase.strategy;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.OAuth2UserInfoRequest;

@Component
public class GitHubProviderStrategy implements OAuth2ProviderStrategy {

    @Override
    public void validateAttributes(Map<String, Object> attributes) {
        if (!attributes.containsKey("email") || attributes.get("email") == null) {
            throw new IllegalArgumentException("GitHub OAuth2: Email é obrigatório.");
        }
        if (!attributes.containsKey("login") || attributes.get("login") == null) {
            throw new IllegalArgumentException("GitHub OAuth2: Login é obrigatório.");
        }
    }

    @Override
    public OAuth2UserInfoRequest extractUserInfo(Map<String, Object> attributes) {
        return OAuth2UserInfoRequest.builder()
            .email((String) attributes.get("email"))
            .name((String) attributes.get("login"))
            .avatar((String) attributes.getOrDefault("avatar_url", null))
            .bio((String) attributes.getOrDefault("bio", null))
            .build();
    }
}