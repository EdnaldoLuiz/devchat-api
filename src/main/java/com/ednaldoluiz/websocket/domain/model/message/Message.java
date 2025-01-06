package com.ednaldoluiz.websocket.domain.model.message;

import com.ednaldoluiz.websocket.domain.model.base.EntityBase;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
    private MessageText messageText;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;
    
}
