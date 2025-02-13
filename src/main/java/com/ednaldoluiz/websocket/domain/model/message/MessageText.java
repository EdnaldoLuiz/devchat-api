package com.ednaldoluiz.websocket.domain.model.message;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
public class MessageText implements Serializable { 

    @Id
    private Long messageId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "message_id")
    @JsonBackReference
    private Message message;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    public MessageText() {}

    public MessageText(String content) {
        this.content = content;
        this.message.setSentAt(LocalDateTime.now());
    }
}
