package com.ednaldoluiz.websocket.infra.persistence;

import org.springframework.stereotype.Repository;

import com.ednaldoluiz.websocket.domain.model.message.MessageStatus;

@Repository
public interface MessageStatusRepository extends BaseRepository<MessageStatus> {
    
}
