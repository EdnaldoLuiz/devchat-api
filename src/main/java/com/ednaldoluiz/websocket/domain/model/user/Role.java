package com.ednaldoluiz.websocket.domain.model.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Getter
@RequiredArgsConstructor
public enum Role implements GrantedAuthority {
    
    ADMIN("ROLE_APP_ADMIN"),
    USER("ROLE_USER");

    private final String authority;

    @Override
    public String getAuthority() {
        return this.authority;
    }
}