package com.ednaldoluiz.websocket.domain.model.chat;

import lombok.Getter;

/**
 * Enum que representa os status de um Chat.
 */

@Getter
public enum ChatStatus {

    ACTIVE,
    ARCHIVED,
    BLOCKED,
    MUTED

}
