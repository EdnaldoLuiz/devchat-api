package com.ednaldoluiz.websocket.domain.service;

import java.util.*;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ednaldoluiz.websocket.domain.event.UsersChatCreatedEvent;
import com.ednaldoluiz.websocket.domain.model.chat.*;
import com.ednaldoluiz.websocket.domain.model.message.*;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.repository.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatDomainService {

    private final ChatRepository chatRepository;
    private final UsersChatsRepository  usersChatsRepository;
    private final MessageStatusRepository msgStatusRepository;
    private final ApplicationEventPublisher events;

    @Transactional
    public Chat getOrCreatePrivateChat(User from, User to) {
        var ids = List.of(from.getId(), to.getId()).stream().sorted().toList();
        return chatRepository.findPrivateBetween(ids.get(0), ids.get(1))
                .orElseGet(() -> createPrivateChat(from, to));
    }

    private Chat createPrivateChat(User from, User to) {
        Chat chat = new Chat();
        chat.setType(ChatType.PRIVATE);
        chat.setName(from.getName() + " & " + to.getName());
        chatRepository.persist(chat);

        usersChatsRepository.persistAll(List.of(
                buildUsersChat(from, chat),
                buildUsersChat(to, chat)));

        events.publishEvent(new UsersChatCreatedEvent(chat.getId(), from.getId()));
        events.publishEvent(new UsersChatCreatedEvent(chat.getId(), to.getId()));
        log.debug("Chat PRIVATE {} criado entre {} e {}", chat.getId(), from.getId(), to.getId());
        return chat;
    }

    private UsersChat buildUsersChat(User user, Chat chat) {
        UsersChat usersChat = new UsersChat();
        usersChat.setUser(user);
        usersChat.setChat(chat);
        return usersChat;
    }

    @Transactional
    public void updateChatStatus(Long chatId, Long userId, ChatStatus newStatus) {

        UsersChat usersChat = usersChatsRepository.findByChatIdAndUserId(chatId, userId)
                .orElseThrow(() -> new IllegalStateException("Chat não encontrado"));

        usersChat.setStatus(newStatus);
        usersChatsRepository.persist(usersChat);
    }

    @Transactional
    public void saveMessageStatus(Message msg, User user, MessageStatusType st) {
        msgStatusRepository.persist(new MessageStatus(user, msg, st));
    }
}