package com.ednaldoluiz.websocket.domain.model.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ednaldoluiz.websocket.domain.model.base.TimestampedEntityBase;

@Getter
@Entity
@Table(name = "users", schema = "websocket")
@NoArgsConstructor
public class User extends TimestampedEntityBase implements UserDetails {

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "email", nullable = false, length = 70, unique = true)
    private String email;

    @Setter
    @Column(name = "password", nullable = false, length = 255)
    private char[] password;

    @Column(name = "phone", nullable = false, length = 20, unique = true)
    private String phone;

    @Column(name = "avatar", length = 255)
    private String avatar;

    @Column(name = "bio", length = 255)
    private String bio;

    @Column(name = "deleted", nullable = false, columnDefinition = "boolean default false")
    private boolean deleted;

    @Enumerated(EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
        name = "user_roles", 
        joinColumns = @JoinColumn(
            name = "user_id",
            referencedColumnName = "id"
        )
    )
    @Column(name = "role")
    private Set<Role> roles = new HashSet<>();

    @Column(name = "auth_provider", nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthProvider authProvider = AuthProvider.EMAIL_PASSWORD;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PasswordResetToken> passwordResetTokens;

    public User(String email, String hashedPassword, String name) {
        this.email = email;
        this.password = hashedPassword.toCharArray();
        this.name = name;
        this.roles = Collections.singleton(Role.USER);
    }

    public User(String email, String name, String avatar, String bio, AuthProvider authProvider) {
        this.email = email;
        this.name = name;
        this.authProvider = authProvider;
        this.avatar = avatar;
        this.bio = bio;
        this.password = new char[0];
        this.roles = Collections.singleton(Role.USER);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return new String(this.password);
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    public void softDelete() {
        this.deleted = true;
    }

    public void clearPassword() {
        Arrays.fill(password, '\0');
    }
}