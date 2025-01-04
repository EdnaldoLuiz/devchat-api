package com.ednaldoluiz.websocket.domain.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

import com.ednaldoluiz.websocket.domain.model.base.EntityBase;
import com.ednaldoluiz.websocket.domain.model.room.UsersRooms;
import com.ednaldoluiz.websocket.shared.generator.SnowflakeIdGenerator;

@Getter
@Setter
@Entity
@Table(name = "users", schema = "websocket")
public class User extends EntityBase {

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "phone_number", nullable = false, length = 20, unique = true)
    private String phone;

    @Column(name = "deleted", nullable = false, columnDefinition = "boolean default false")
    private boolean deleted;

    @Transient
    private UserStatus status = UserStatus.OFFLINE;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<UsersRooms> userRooms;

    public User(SnowflakeIdGenerator idGenerator) {
        super(idGenerator);
    }
}