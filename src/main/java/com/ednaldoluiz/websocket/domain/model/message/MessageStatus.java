package com.ednaldoluiz.websocket.domain.model.message;

import java.time.LocalDateTime;

import com.ednaldoluiz.websocket.domain.model.base.EntityBase;
import com.ednaldoluiz.websocket.domain.model.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "MessageStatus")
@Table(
    name = "message_status", 
    schema = "websocket",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_message_status_msg_user", columnNames = { "message_id", "user_id" })
})
public class MessageStatus extends EntityBase {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "message_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_message_status"),
        nullable = false
    )
    private Message message;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_user_status"), 
        nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, columnDefinition = "ENUM('DELIVERED', 'READ', 'DELETED', 'FAILED', 'PENDING')")
    private MessageStatusType status;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    public MessageStatus(User user, Message message, MessageStatusType status) {
        this.user = user;
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }

    public void markRead() {
        this.status = MessageStatusType.READ;
        this.timestamp = LocalDateTime.now();
    }

    public void markDelivered() {
        this.status = MessageStatusType.SENT;
        this.timestamp = LocalDateTime.now();
    }
}