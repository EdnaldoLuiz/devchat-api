package com.ednaldoluiz.websocket.domain.model.message;

import java.time.LocalDateTime;
import java.util.Optional;

import com.ednaldoluiz.websocket.domain.model.base.EntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "MessageText")
@Table(name = "message_texts", schema = "websocket")
public class MessageText extends EntityBase {

    @OneToOne
    @MapsId
    @JoinColumn(name = "message_id")
    private Message message;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    public MessageText() {}

    public MessageText(String content) {
        this.content = content;
        this.message.setSentAt(LocalDateTime.now());
    }

    public MessageText(Message parent, String content) {
        this.message = parent;
        this.content = content;
    }

    public static Optional<MessageText> of(Message message, String content) {
        if (content == null || content.isBlank()) return Optional.empty();

        MessageText text = new MessageText();
        text.setMessage(message);
        text.setContent(content);
        return Optional.of(text);
    }
}
