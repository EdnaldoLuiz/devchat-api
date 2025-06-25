package com.ednaldoluiz.websocket.domain.model.message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ednaldoluiz.websocket.domain.model.base.EntityBase;
import com.ednaldoluiz.websocket.domain.model.chat.Chat;
import com.ednaldoluiz.websocket.domain.model.user.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "Message")
@Table(name = "messages", schema = "websocket")
public class Message extends EntityBase {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "message", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<MessageAttachments> attachments = new ArrayList<>();

    @Column(name = "message_uuid", columnDefinition = "BINARY(16)", unique = true, updatable = false)
    private UUID messageUuid;

    @Lob
    @Column(name = "cipher_body", nullable = false, columnDefinition = "MEDIUMBLOB")
    private byte[] cipherBody;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    public Message() {}

    public Message(UUID uuid, Chat chat, User sender, byte[] cipherBody) {
        this.messageUuid = uuid;
        this.chat        = chat;
        this.user        = sender;
        this.cipherBody  = cipherBody;
        this.sentAt      = LocalDateTime.now();
        this.deleted     = false;
    }

    public void addAttachment(MessageAttachments attachment) {
        if (this.attachments == null)
            this.attachments = new ArrayList<>();
        this.attachments.add(attachment);
    }
}
