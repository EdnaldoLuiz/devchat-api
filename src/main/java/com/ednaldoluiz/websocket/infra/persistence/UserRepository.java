package com.ednaldoluiz.websocket.infra.persistence;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ednaldoluiz.websocket.domain.model.user.User;

@Repository
public interface UserRepository extends BaseRepository<User> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);
    
}
