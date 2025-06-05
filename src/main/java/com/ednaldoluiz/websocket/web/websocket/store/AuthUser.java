package com.ednaldoluiz.websocket.web.websocket.store;

import com.ednaldoluiz.websocket.domain.model.user.Role;
import com.ednaldoluiz.websocket.domain.model.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

public record AuthUser(

        Long id, 
        String email, 
        String name, 
        String password, 
        Set<Role> roles
        
    ) implements UserDetails {

    public AuthUser(User entity) {
        this(entity.getId(), entity.getEmail(), entity.getName(), entity.getPassword(), entity.getRoles());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }
}
