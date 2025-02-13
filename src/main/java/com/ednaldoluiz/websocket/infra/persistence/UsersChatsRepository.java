package com.ednaldoluiz.websocket.infra.persistence;

import org.springframework.stereotype.Repository;

import com.ednaldoluiz.websocket.domain.model.chat.UsersChat;

@Repository
public interface UsersChatsRepository extends BaseRepository<UsersChat> {}