package com.ednaldoluiz.websocket.domain.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ednaldoluiz.websocket.domain.model.base.TimestampedEntityBase;
import com.ednaldoluiz.websocket.domain.model.room.UsersRooms;
import com.ednaldoluiz.websocket.shared.generator.SnowflakeIdGenerator;

@Getter
@Setter
@Entity
@Table(name = "users", schema = "websocket")
public class User extends TimestampedEntityBase implements UserDetails {

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "email", nullable = false, length = 255, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private char[] password;

    @Column(name = "phone_number", nullable = false, length = 20, unique = true)
    private String phone;

    @Column(name = "deleted", nullable = false, columnDefinition = "boolean default false")
    private boolean deleted;

    @Transient
    private UserStatus status = UserStatus.OFFLINE;

    @Transient
    private UserAgentInfo userAgentInfo;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private Set<UsersRooms> userRooms;

    public User(SnowflakeIdGenerator idGenerator) {
        super(idGenerator);
    }

    public void clearPassword() {
        Arrays.fill(password, '\0'); // Limpa a senha após o uso
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return new String(this.password);
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}