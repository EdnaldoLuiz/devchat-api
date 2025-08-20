package com.ednaldoluiz.websocket.domain.model.message;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.ednaldoluiz.websocket.domain.message.valueObject.HistoryContext;
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

    @OneToMany(mappedBy = "message", cascade = { CascadeType.MERGE, CascadeType.PERSIST,
            CascadeType.REMOVE }, orphanRemoval = true)
    private List<MessageAttachments> attachments = new ArrayList<>();

    @Column(name = "message_uuid", columnDefinition = "BINARY(16)", unique = true, updatable = false)
    private UUID messageUuid;

    @Lob
    @Column(name = "history_ciphertext", columnDefinition = "MEDIUMBLOB")
    private byte[] historyCiphertext;

    @Column(name = "history_initialization_vector", columnDefinition = "VARBINARY(12)")
    private byte[] historyInitializationVector;

    @Column(name = "history_algorithm", length = 32)
    private String historyAlgorithm;

    @Column(name = "history_version")
    private Integer historyVersion;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt;

    public Message() {}

    private Message(Chat chat, User sender, UUID uuid, LocalDateTime sentAt) {
        this.chat = Objects.requireNonNull(chat, "chat");
        this.user = Objects.requireNonNull(sender, "sender");
        this.messageUuid = Objects.requireNonNull(uuid, "messageUuid");
        this.sentAt = Objects.requireNonNullElseGet(sentAt, LocalDateTime::now);
        this.deleted = false;
    }

    public static Message create(Chat chat, User sender, UUID uuid, LocalDateTime sentAt, HistoryContext history) {
        Objects.requireNonNull(history, "history");
        Message message = new Message(chat, sender, uuid, sentAt);
        message.applyHistory(history);
        return message;
    }

    private void applyHistory(HistoryContext history) {
        this.historyAlgorithm = history.algorithm();
        this.historyVersion = history.version();
        this.historyInitializationVector = history.initializationVector();
        this.historyCiphertext = history.ciphertext();
    }

    public void addAttachment(MessageAttachments attachment) {
        if (this.attachments == null)
            this.attachments = new ArrayList<>();
        this.attachments.add(attachment);
    }
}
