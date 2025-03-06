package com.ednaldoluiz.websocket.app.v1.auth.usecase.strategy;

import java.util.Map;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.OAuth2UserInfoRequest;

public interface OAuth2ProviderStrategy {
    
    void validateAttributes(Map<String, Object> attributes);

    OAuth2UserInfoRequest extractUserInfo(Map<String, Object> attributes);
    
}
