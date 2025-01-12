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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "MessageStatus")
@Table(name = "message_status", schema = "websocket")
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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

}