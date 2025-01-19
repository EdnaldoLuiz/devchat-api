package com.ednaldoluiz.websocket.infra.security.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public User getUserById(Long id) {
        return userRepository
            .findById(id)
            .orElseThrow(() -> new UsernameNotFoundException("Token is not valid"));
    }

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository
            .findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("Credentials is not valid"));
    }
}
