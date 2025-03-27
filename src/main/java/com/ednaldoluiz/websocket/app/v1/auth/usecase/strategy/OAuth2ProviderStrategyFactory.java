package com.ednaldoluiz.websocket.app.v1.auth.usecase.strategy;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.domain.model.user.AuthProvider;

@Component
public class OAuth2ProviderStrategyFactory {

    private final Map<AuthProvider, OAuth2ProviderStrategy> strategies = new EnumMap<>(AuthProvider.class);

    public OAuth2ProviderStrategyFactory(
        GitHubProviderStrategy gitHubProviderStrategy,
        GoogleProviderStrategy googleProviderStrategy
    ) {
        strategies.put(AuthProvider.GITHUB, gitHubProviderStrategy);
        strategies.put(AuthProvider.GOOGLE, googleProviderStrategy);
    }

    public OAuth2ProviderStrategy getStrategy(AuthProvider provider) {
        return strategies.get(provider);
    }
}
