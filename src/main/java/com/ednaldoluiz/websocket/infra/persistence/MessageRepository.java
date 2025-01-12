package com.ednaldoluiz.websocket.infra.persistence;

import org.springframework.stereotype.Repository;

import com.ednaldoluiz.websocket.domain.model.message.Message;

@Repository
public interface MessageRepository extends BaseRepository<Message> {

    
}