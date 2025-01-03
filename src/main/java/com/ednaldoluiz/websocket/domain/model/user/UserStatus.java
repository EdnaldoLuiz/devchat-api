package com.ednaldoluiz.websocket.domain.model.user;

import com.ednaldoluiz.websocket.shared.utils.EnumUtils;
import lombok.Getter;

/**
 * Enum que representa o status do usuário.
 * Possui os valores ONLINE e OFFLINE, com métodos utilitários para conversão e
 * manipulação.
 */
@Getter
public enum UserStatus {

    ONLINE("online"),
    OFFLINE("offline");

    private final String status;

    UserStatus(String status) {
        this.status = status;
    }

    public static UserStatus fromString(String status) {
        return EnumUtils.fromString(UserStatus.class, status, OFFLINE);
    }

    /**
     * Retorna a representação textual do status, evitando conlitos.
     *
     * @return String representando o status.
     */
    @Override
    public String toString() {
        return this.status;
    }
}
