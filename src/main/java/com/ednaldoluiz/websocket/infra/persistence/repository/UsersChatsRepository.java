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

    @Query("select uc from UsersChat uc where uc.chat.id = :chatId")
    List<UsersChat> findAllByChatId(@Param("chatId") Long chatId);

}