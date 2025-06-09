package com.ednaldoluiz.websocket.infra.persistence.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ednaldoluiz.websocket.domain.model.chat.Chat;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends BaseRepository<Chat> {

    @Query("""
        SELECT c
        FROM Chat c
        JOIN UsersChat uc1 ON uc1.chat = c
        JOIN UsersChat uc2 ON uc2.chat = c
        WHERE c.type = 'PRIVATE'
        AND uc1.user.id = :a
        AND uc2.user.id = :b
    """)
    Optional<Chat> findPrivateBetween(@Param("a") Long a, @Param("b") Long b);

    @Query("""
        SELECT uc.user.email
          FROM UsersChat uc
         WHERE uc.chat.id = :chatId
        """)
    List<String> membersEmails(@Param("chatId") Long chatId);
    
}
