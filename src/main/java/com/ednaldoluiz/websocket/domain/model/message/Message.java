package com.ednaldoluiz.websocket.domain.model.message;

import java.time.LocalDateTime;

import com.ednaldoluiz.websocket.domain.model.base.EntityBase;
import com.ednaldoluiz.websocket.domain.model.chat.Chat;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "Message")
@Table(name = "messages", schema = "websocket")
public class Message extends EntityBase {

    @OneToOne(
        mappedBy = "message", 
        cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE }, 
        fetch = FetchType.LAZY
    )
    @JsonManagedReference
    private MessageText messageText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "chat_id", 
        nullable = false
    )
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    public Message() {
        this.deleted = false;
    }
    // public Message(SendMessageRequest request, User user, Chat chat) {
    //     this.messageText = new MessageText(request.message());
    //     this.chat = chat;
    //     this.user = user;
    //     this.sentAt = LocalDateTime.now();
    //     this.deleted = false;
    // }
}
