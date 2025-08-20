// src/main/java/com/ednaldoluiz/websocket/infra/persistence/repository/MessageRepository.java
package com.ednaldoluiz.websocket.infra.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatMessageResponse;
import com.ednaldoluiz.websocket.domain.model.message.Message;

@Repository
public interface MessageRepository extends BaseRepository<Message> {

    @Query("""
                SELECT new com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatMessageResponse(
                    m.id,
                    m.messageUuid,
                    m.user.id,
                    :meId, 
                    m.historyAlgorithm,
                    m.historyVersion,
                    m.historyInitializationVector,
                    m.historyCiphertext,
                    ma.attachmentType,
                    m.sentAt
                )
                FROM Message m
                LEFT JOIN MessageAttachments ma ON ma.message.id = m.id
                WHERE m.chat.id = :chatId
                ORDER BY m.sentAt ASC, m.id ASC
            """)
    Page<ChatMessageResponse> findHistoryPage(
            @Param("chatId") Long chatId,
            @Param("meId") Long meId,
            Pageable pageable);

}