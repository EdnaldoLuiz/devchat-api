package com.ednaldoluiz.websocket.infra.security.service;

import com.ednaldoluiz.websocket.web.websocket.store.AuthUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Slf4j
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
    //@Cacheable(cacheNames = "userDetailsCache", key = "#username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User entity = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas"));
        log.info("Load user by username: {}, id {}", username, entity.getId());
        return new AuthUser(entity);
    }
}
