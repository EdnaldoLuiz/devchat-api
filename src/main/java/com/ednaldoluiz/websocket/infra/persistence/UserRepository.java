package com.ednaldoluiz.websocket.infra.persistence;

import org.springframework.stereotype.Repository;

import com.ednaldoluiz.websocket.domain.model.user.User;

@Repository
public interface UserRepository extends BaseRepository<User> {
    
}
