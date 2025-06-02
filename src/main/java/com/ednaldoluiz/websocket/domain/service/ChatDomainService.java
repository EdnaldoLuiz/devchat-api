package com.ednaldoluiz.websocket.domain.service;

import com.ednaldoluiz.websocket.domain.model.chat.Chat;
import com.ednaldoluiz.websocket.domain.model.chat.ChatStatus;
import com.ednaldoluiz.websocket.domain.model.chat.ChatType;
import com.ednaldoluiz.websocket.domain.model.chat.UsersChat;
import com.ednaldoluiz.websocket.domain.model.message.Message;
import com.ednaldoluiz.websocket.domain.model.message.MessageStatus;
import com.ednaldoluiz.websocket.domain.model.message.MessageStatusType;
import com.ednaldoluiz.websocket.domain.model.notification.NotificationType;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.ChatRepository;
import com.ednaldoluiz.websocket.infra.persistence.MessageStatusRepository;
import com.ednaldoluiz.websocket.infra.persistence.UsersChatsRepository;
import com.ednaldoluiz.websocket.web.controller.v1.chat.ChatController.NotificationDto;

import lombok.RequiredArgsConstructor;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChatDomainService {

    private final ChatRepository chatRepository;
    private final UsersChatsRepository usersChatsRepository;
    private final MessageStatusRepository messageStatusRepository;

    @Transactional
    public Chat getOrCreatePrivateChat(User userA, User userB) {
        List<Long> ids = Arrays.asList(userA.getId(), userB.getId());
        Collections.sort(ids);

        return chatRepository.findPrivateBetween(ids.get(0), ids.get(1))
                .orElseGet(() -> createPrivateChat(userA, userB));
    }

    private Chat createPrivateChat(User userA, User userB) {
        Chat chat = new Chat();
        chat.setType(ChatType.PRIVATE);
        chat.setName(userA.getName() + " & " + userB.getName());
        chatRepository.save(chat);

        usersChatsRepository.saveAll(List.of(
                createUsersChat(userA, chat),
                createUsersChat(userB, chat)));

        return chat;
    }

    private UsersChat createUsersChat(User user, Chat chat) {
        UsersChat usersChat = new UsersChat();
        usersChat.setUser(user);
        usersChat.setChat(chat);
        return usersChat;
    }

    @Transactional
    public void updateChatStatus(Long chatId, User user, ChatStatus status) {
        UsersChat usersChat = usersChatsRepository.findByChatIdAndUserId(chatId, user.getId())
                .orElseThrow(() -> new IllegalStateException("Chat não encontrado para o usuário"));
        usersChat.setStatus(status);
        usersChatsRepository.save(usersChat);
    }

    @Transactional
    public void saveMessageStatus(Message message, User user, MessageStatusType statusType) {
        MessageStatus status = new MessageStatus(user, message, statusType);
        messageStatusRepository.save(status);
    }

    @Transactional
    public Message newMessage(Chat chat, User sender, UUID messageUuid) {
        Message m = new Message();
        m.setChat(chat);
        m.setUser(sender);
        m.setMessageUuid(messageUuid);
        m.setSentAt(LocalDateTime.now());
        return m;
    }

    @Transactional
    public void saveStatuses(Message m, User from, User to) {
        saveMessageStatus(m, from, MessageStatusType.DELIVERED);
        saveMessageStatus(m, to, MessageStatusType.PENDING);
    }

    public void broadcastAndNotify(
            User from, User to, Object payload, SimpMessagingTemplate template) {

        template.convertAndSendToUser(from.getEmail(), "/queue/messages", payload);
        template.convertAndSendToUser(to.getEmail(), "/queue/messages", payload);

        template.convertAndSendToUser(to.getEmail(), "/queue/notify",
                new NotificationDto(to.getEmail(), NotificationType.MESSAGE, from.getEmail())
        );
    }
}
