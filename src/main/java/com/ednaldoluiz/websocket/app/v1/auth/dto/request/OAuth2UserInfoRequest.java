package com.ednaldoluiz.websocket.app.v1.auth.dto.request;

import lombok.Builder;

@Builder
public record OAuth2UserInfoRequest(
    String email,
    String name,
    String avatar,
    String bio
) {}