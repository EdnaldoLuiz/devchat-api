package com.ednaldoluiz.websocket.domain.service;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

import com.ednaldoluiz.websocket.domain.event.MessageCreatedEvent;
import com.ednaldoluiz.websocket.domain.event.MessageStatusChangedEvent;
import com.ednaldoluiz.websocket.domain.event.UsersChatCreatedEvent;
import com.ednaldoluiz.websocket.domain.model.chat.ChatSummary;
import com.ednaldoluiz.websocket.domain.model.chat.ChatType;
import com.ednaldoluiz.websocket.domain.model.message.MessageStatusType;
import com.ednaldoluiz.websocket.infra.persistence.projection.NameAvatarProjection;
import com.ednaldoluiz.websocket.infra.persistence.repository.ChatSummaryRepository;
import com.ednaldoluiz.websocket.infra.persistence.repository.UserRepository;
import com.ednaldoluiz.websocket.infra.persistence.repository.UsersChatsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ChatSummaryService {

    private static final int DEFAULT_UNREAD_COUNT = 0;

    private final ChatSummaryRepository chatSummaryRepository;
    private final UsersChatsRepository usersChatsRepository;
    private final UserRepository userRepository;

    
}
