package com.ednaldoluiz.websocket.infra.persistence.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ednaldoluiz.websocket.domain.model.chat.UsersChat;

@Repository
public interface UsersChatsRepository extends BaseRepository<UsersChat> {

    @Query("""
        SELECT uc FROM UsersChat uc 
        WHERE uc.chat.id = :chatId 
        AND uc.user.id = :id
        """)
    Optional<UsersChat> findByChatIdAndUserId(Long chatId, Long id);

    @Query("""
        SELECT uc.user.id
          FROM UsersChat uc
         WHERE uc.chat.id = :chatId
           AND uc.user.id <> :myId
        """)
    Long findOtherParticipant(Long chatId, Long myId);

    @Query("SELECT uc.user.id FROM UsersChat uc WHERE uc.chat.id = :chatId")
    List<Long> findParticipantIds(Long chatId);

    @Query("SELECT uc FROM UsersChat uc WHERE uc.chat.id = :chatId")
    List<UsersChat> findAllByChatId(@Param("chatId") Long chatId);

}