package com.ednaldoluiz.websocket.domain.model.message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ednaldoluiz.websocket.domain.model.base.EntityBase;
import com.ednaldoluiz.websocket.domain.model.chat.Chat;
import com.ednaldoluiz.websocket.domain.model.user.User;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
        cascade = { CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE }
    )
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

    @OneToMany(mappedBy = "message", cascade = { 
        CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE 
    }, orphanRemoval = true)
    private List<MessageAttachments> attachments = new ArrayList<>();

    @Column(name = "message_uuid", columnDefinition = "BINARY(16)", unique = true, updatable = false)
    private UUID messageUuid;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    public Message() {
        this.deleted = false;
    }

    public void addText(String content) {
        if (content == null || content.isBlank()) return;
        this.messageText = new MessageText(this, content);
    }

    public void addAttachment(MessageAttachmentType type, String url) {
        if (this.attachments == null) this.attachments = new ArrayList<>();
        this.attachments.add(new MessageAttachments(this, type, url));
    }

    public static Message ofText(Chat chat, User sender, UUID uuid, String content) {
        Message message = new Message(uuid, chat, sender);
        message.messageText = new MessageText(message, content);
        return message;
    }

    public Message(UUID uuid, Chat chat, User sender) {
        this.messageUuid = uuid;
        this.chat        = chat;
        this.user        = sender;
        this.sentAt      = LocalDateTime.now();
        this.deleted     = false;
    }
}
