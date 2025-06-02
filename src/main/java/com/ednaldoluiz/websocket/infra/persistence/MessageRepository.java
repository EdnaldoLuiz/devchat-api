package com.ednaldoluiz.websocket.infra.persistence;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ednaldoluiz.websocket.domain.model.message.Message;

@Repository
public interface MessageRepository extends BaseRepository<Message> {

    @Query("""
        select m from Message m
        left join fetch m.messageText
        where m.chat.id = :chatId
        order by m.sentAt desc
    """)
    Page<Message> findByChatIdFetchText(@Param("chatId") Long chatId, Pageable pageable);
    
}