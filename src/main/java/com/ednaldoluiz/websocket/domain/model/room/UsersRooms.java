package com.ednaldoluiz.websocket.domain.model.room;

import java.time.LocalDateTime;

import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.shared.generator.SnowflakeIdGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(
    name = "users_rooms",
    schema = "websocket",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_users_rooms_user_room", columnNames = { "user_id", "room_id" })
    }
)
public class UsersRooms {

    @Id
    private Long id;

    @ManyToOne(
        fetch = FetchType.LAZY, 
        optional = false, 
        cascade = { CascadeType.PERSIST, CascadeType.MERGE }
    )
    @JoinColumn(
        name = "user_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_users_rooms_user", value = ConstraintMode.CONSTRAINT)
    )
    private User user;

    @ManyToOne(
        fetch = FetchType.LAZY, 
        optional = false, 
        cascade = { CascadeType.PERSIST, CascadeType.MERGE }
    )
    @JoinColumn(
        name = "room_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_users_rooms_room", value = ConstraintMode.CONSTRAINT)
    )
    private Room room;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20, columnDefinition = "ENUM('ADMIN','MEMBER')")
    private RoomRole role;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "joined_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime joinedAt;

    public UsersRooms(SnowflakeIdGenerator idGenerator) {
        this.id = idGenerator.generateId();
    }
}