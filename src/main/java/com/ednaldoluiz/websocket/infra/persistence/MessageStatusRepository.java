package com.ednaldoluiz.websocket.infra.persistence;

import com.ednaldoluiz.websocket.domain.model.message.MessageStatusType;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ednaldoluiz.websocket.domain.model.message.MessageStatus;

import java.time.LocalDateTime;

@Repository
public interface MessageStatusRepository extends BaseRepository<MessageStatus> {

    @Modifying
    @Transactional
    @Query("""
        UPDATE MessageStatus ms
           SET ms.status    = :newStatus,
               ms.timestamp = :readTime
         WHERE ms.message.chat.id = :chatId
           AND ms.user.id         = :userId
           AND ms.status          = :oldStatus
        """)
    void markChatMessagesAsRead(
            @Param("chatId")    Long chatId,
            @Param("userId")    Long userId,
            @Param("readTime") LocalDateTime readTime,
            @Param("oldStatus") MessageStatusType oldStatus,
            @Param("newStatus") MessageStatusType newStatus
    );
}
