package com.ednaldoluiz.websocket.infra.persistence;

import org.springframework.stereotype.Repository;

import com.ednaldoluiz.websocket.domain.model.chat.Chat;

@Repository
public interface ChatRepository extends BaseRepository<Chat> {
    
}
