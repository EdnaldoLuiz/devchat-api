package com.ednaldoluiz.websocket.domain.model.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
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
@NoArgsConstructor
public class User extends TimestampedEntityBase implements UserDetails {

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "email", nullable = false, length = 70, unique = true)
    private String email;

    @Column(name = "password", nullable = false, length = 255)
    private char[] password;

    @Column(name = "phone", nullable = false, length = 20, unique = true)
    private String phone;

    @Column(name = "avatar", length = 255)
    private String avatar;

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

    public User(SnowflakeIdGenerator idGenerator, String email, String hashedPassword, String name, String phone, String avatar) {
        super(idGenerator);
        this.email = email;
        this.password = hashedPassword.toCharArray();
        this.name = name;
        this.phone = phone;
        this.avatar = avatar;
    }

    public void clearPassword() {
        Arrays.fill(password, '\0'); // Limpa a senha após o uso
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
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