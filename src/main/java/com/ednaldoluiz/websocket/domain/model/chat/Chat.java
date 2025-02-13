package com.ednaldoluiz.websocket.domain.model.chat;

import com.ednaldoluiz.websocket.domain.model.base.TimestampedEntityBase;
import com.ednaldoluiz.websocket.shared.generator.SnowflakeIdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Representa um Chat no sistema.
 * Pode ser do tipo PRIVATE (privado) ou GROUP (grupo).
 */
@Getter
@Setter
@Entity
@Table(name = "chats")
public class Chat extends TimestampedEntityBase {

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "ENUM('PRIVATE', 'GROUP')")
    private ChatType type;

    public Chat() {
        super();
    }

    public Chat(SnowflakeIdGenerator idGenerator) {
        super(idGenerator);
    }
}
