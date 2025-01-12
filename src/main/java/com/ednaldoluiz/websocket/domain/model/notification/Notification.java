package com.ednaldoluiz.websocket.domain.model.notification;

import java.time.LocalDateTime;

import com.ednaldoluiz.websocket.domain.model.base.EntityBase;
import com.ednaldoluiz.websocket.domain.model.message.Message;
import com.ednaldoluiz.websocket.domain.model.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "Notification")
@Table(name = "notifications", schema = "websocket")
public class Notification extends EntityBase {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "user_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_user_notification"),
        nullable = false
    )
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "message_id",
        referencedColumnName = "id",
        foreignKey = @ForeignKey(name = "fk_message_notification"),
        nullable = true
    )
    private Message message;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "ENUM('MESSAGE', 'GROUP_INVITE')")
    private NotificationType type;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "read_at")
    private LocalDateTime readAt;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

}