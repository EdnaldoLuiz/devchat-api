package com.ednaldoluiz.websocket.domain.model.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.ednaldoluiz.websocket.domain.model.base.EntityBase;
import com.ednaldoluiz.websocket.shared.generator.SnowflakeIdGenerator;

import jakarta.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "message_attachments", schema = "websocket", uniqueConstraints = {
    @UniqueConstraint(name = "uk_message_attachments_url", columnNames = { "url" })
})
public class MessageAttachments extends EntityBase {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
        name = "message_id", 
        nullable = false, 
        foreignKey = @ForeignKey(name = "fk_message_attachments_message")
    )
    private Message message;

    @Enumerated(EnumType.STRING)
    @Column(
        name = "attachment_type", 
        nullable = false, 
        columnDefinition = "ENUM('IMAGE', 'VIDEO', 'FILE', 'AUDIO')"
    )
    private MessageAttachmentType attachmentType;

    @Column(name = "url", length = 512, nullable = false, updatable = false)
    private String url;

    @Lob
    @Column(name = "attachment_data", columnDefinition = "MEDIUMBLOB")
    private byte[] attachmentData;

    public MessageAttachments(SnowflakeIdGenerator idGenerator) {
        super(idGenerator);
    }
}
