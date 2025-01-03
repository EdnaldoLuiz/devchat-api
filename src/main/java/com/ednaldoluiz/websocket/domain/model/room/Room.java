package com.ednaldoluiz.websocket.domain.model.room;

import com.ednaldoluiz.websocket.domain.model.base.EntityBase;
import com.ednaldoluiz.websocket.shared.generator.SnowflakeIdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "Room")
@Table(name = "rooms", schema = "websocket")
public class Room extends EntityBase {

    @Column(name = "name", nullable = false, length = 100, unique = true)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "icon", length = 255)
    private String icon;

    @Column(name = "deleted", nullable = false)
    private boolean deleted;

    public Room(SnowflakeIdGenerator idGenerator) {
        super(idGenerator);
    }
}