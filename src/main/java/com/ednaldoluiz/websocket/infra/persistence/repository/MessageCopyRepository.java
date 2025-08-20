// src/main/java/com/ednaldoluiz/websocket/infra/persistence/repository/MessageCopyRepository.java
package com.ednaldoluiz.websocket.infra.persistence.repository;

import org.springframework.data.domain.*;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatMessageResponse;
import com.ednaldoluiz.websocket.domain.model.message.MessageCopy;

@Repository
public interface MessageCopyRepository extends BaseRepository<MessageCopy> {

  
}
