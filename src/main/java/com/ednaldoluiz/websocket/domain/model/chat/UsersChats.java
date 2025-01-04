package com.ednaldoluiz.websocket.domain.model.chat;

import com.ednaldoluiz.websocket.domain.model.base.EntityBase;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.shared.generator.SnowflakeIdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Representa a relação entre usuários e chats.
 * Armazena o estado do chat para o usuário (ativo, arquivado, bloqueado, etc.).
 */
@Entity
@Table(
  name = "users_chats",
  uniqueConstraints = {
    @UniqueConstraint(
      name = "uk_users_chats_user_chat",
      columnNames = { "user_id", "chat_id" }
    )
  }
)
public class UsersChats extends EntityBase {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_users_chats_user")
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "chat_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_users_chats_chat")
    )
    private Chat chat;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "ENUM('ACTIVE','ARCHIVED','BLOCKED','MUTED')")
    private ChatStatus status = ChatStatus.ACTIVE;

    @Column(name = "last_read_message_id", nullable = true)
    private Long lastReadMessageId;

    public UsersChats(SnowflakeIdGenerator idGenerator) {
        super(idGenerator);
    }
}
