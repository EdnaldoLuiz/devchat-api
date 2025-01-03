package com.ednaldoluiz.websocket.domain.model.room;

import java.util.Set;

import com.ednaldoluiz.websocket.domain.model.base.EntityBase;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.shared.generator.SnowflakeIdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Table(name = "users_rooms", schema = "websocket")
@Entity(name = "UsersRooms")
public class UsersRooms extends EntityBase {

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "users_rooms",
        uniqueConstraints = {
            @UniqueConstraint(name = "uk_users_rooms_user_room", columnNames = { "user_id", "room_id" })
        },
        joinColumns = {
            @JoinColumn(
                name = "user_id",
                referencedColumnName = "id",
                table = "users",
                unique = false,
                foreignKey = @ForeignKey(name = "fk_users_rooms_user", value = ConstraintMode.CONSTRAINT)
            )
        },
        inverseJoinColumns = {
            @JoinColumn(
                name = "room_id",
                referencedColumnName = "id",
                table = "rooms",
                unique = false,
                foreignKey = @ForeignKey(name = "fk_users_rooms_room", value = ConstraintMode.CONSTRAINT)
            )
        }
    )
    private Set<User> users;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "users_rooms",
        uniqueConstraints = {
            @UniqueConstraint(name = "uk_users_rooms_user_room", columnNames = { "user_id", "room_id" })
        },
        joinColumns = {
            @JoinColumn(
                name = "user_id",
                referencedColumnName = "id",
                table = "users",
                unique = false,
                foreignKey = @ForeignKey(name = "fk_users_rooms_user", value = ConstraintMode.CONSTRAINT)
            )
        },
        inverseJoinColumns = {
            @JoinColumn(
                name = "room_id",
                referencedColumnName = "id",
                table = "rooms",
                unique = false,
                foreignKey = @ForeignKey(name = "fk_users_rooms_room", value = ConstraintMode.CONSTRAINT)
            )
        }
    )
    private Set<Room> rooms;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20, columnDefinition = "ENUM('ADMIN', 'MEMBER')")
    private RoomRole role;

    public UsersRooms(SnowflakeIdGenerator idGenerator) {
        super(idGenerator);
    }
}
