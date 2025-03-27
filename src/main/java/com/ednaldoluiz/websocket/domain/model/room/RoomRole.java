package com.ednaldoluiz.websocket.domain.model.room;

import lombok.Getter;

/**
 * Enum que representa os papéis dentro de uma sala.
 * Possui os valores ADMIN e MEMBER (referido como USER internamente), com métodos utilitários para conversão e manipulação.
 */
@Getter
public enum RoomRole {

    /**
     * Representa o papel de administrador da sala.
     */
    ADMIN("ADMIN"),

    /**
     * Representa o papel de usuário comum da sala.
     */
    MEMBER("MEMBER");

    private final String name;

    RoomRole(String name) {
        this.name = name;
    }

    public static RoomRole fromString(String role) {
        if (role == null || role.isBlank()) {
            return MEMBER;
        }
        for (RoomRole r : values()) {
            if (r.toString().equalsIgnoreCase(role)) {
                return r;
            }
        }
        return MEMBER;
    }

    /**
     * Retorna a representação textual do papel.
     *
     * @return String representando o papel.
     */
    @Override
    public String toString() {
        return this.name;
    }
}