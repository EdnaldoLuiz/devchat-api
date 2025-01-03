package com.ednaldoluiz.websocket.domain.model.room;

import com.ednaldoluiz.websocket.shared.utils.EnumUtils;

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
        return EnumUtils.fromString(RoomRole.class, role, MEMBER);
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