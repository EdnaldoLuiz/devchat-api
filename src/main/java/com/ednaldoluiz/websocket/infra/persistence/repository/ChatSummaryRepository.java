package com.ednaldoluiz.websocket.infra.persistence.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.ednaldoluiz.websocket.domain.model.chat.ChatSummary;
import com.ednaldoluiz.websocket.domain.model.chat.ChatSummaryId;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;

public interface ChatSummaryRepository extends BaseJpaRepository<ChatSummary, ChatSummaryId> {

    @Modifying
    @Query("""
            UPDATE ChatSummary cs SET
                cs.lastMessageId       = :msgId,
                cs.lastMessageAt       = :at,
                cs.lastMessageSenderId = :sender,
                cs.lastMessageContent  = :content
            WHERE cs.userId = :userId
              AND cs.chatId = :chatId
            """)
    void updateLastForUser(Long userId, Long chatId,
            Long msgId, LocalDateTime at, Long sender, String content);

    @Modifying
    @Query("""
            UPDATE ChatSummary cs SET
                cs.unreadCount = LEAST(GREATEST(cs.unreadCount + :delta, 0), 999)
            WHERE cs.userId = :userId
              AND cs.chatId = :chatId
            """)
    void addUnread(Long userId, Long chatId, int delta);

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO chat_summaries
                (user_id, chat_id,
                 participant_id, participant_name, participant_avatar,
                 unread_count)
            VALUES
                (:ownerId, :chatId,
                 :counterId, :counterName, :counterAvatar, 0),
                (:counterId, :chatId,
                 :ownerId,  :ownerName,  :ownerAvatar,  0)
            ON DUPLICATE KEY UPDATE
                participant_name   = VALUES(participant_name),
                participant_avatar = VALUES(participant_avatar)
            """, nativeQuery = true)
    void upsertPair(
            Long ownerId,
            Long chatId,
            Long counterId,
            String counterName,
            String counterAvatar,
            String ownerName,
            String ownerAvatar);

    Optional<ChatSummary> findByUserIdAndChatId(Long userId, Long chatId);

    @Query("""
            SELECT cs FROM ChatSummary cs
            WHERE cs.userId = :userId
            ORDER BY cs.lastMessageAt DESC
            """)
    List<ChatSummary> findAllByUserId(Long userId);

}