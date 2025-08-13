package com.ednaldoluiz.websocket.infra.persistence.projection;

public record NameAvatarProjection(
    Long userId,
    String name,
    String avatar
) {

    public static NameAvatarProjection of(Long userId, String name, String avatar) {
        return new NameAvatarProjection(userId, name, avatar);
    }

    public static NameAvatarProjection of(Long userId) {
        return new NameAvatarProjection(userId, null, null);
    }
}