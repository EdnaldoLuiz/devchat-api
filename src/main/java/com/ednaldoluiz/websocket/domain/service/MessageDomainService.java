package com.ednaldoluiz.websocket.domain.service;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatMessageResponse;
import com.ednaldoluiz.websocket.domain.model.message.Message;
import com.ednaldoluiz.websocket.domain.model.message.MessageStatus;
import com.ednaldoluiz.websocket.domain.model.message.MessageStatusType;
import com.ednaldoluiz.websocket.domain.model.notification.NotificationType;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.MessageRepository;
import com.ednaldoluiz.websocket.infra.persistence.MessageStatusRepository;
import com.ednaldoluiz.websocket.web.controller.v1.chat.ChatController.NotificationDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageDomainService {

    private final MessageRepository      msgRepo;
    private final MessageStatusRepository statusRepo;
    private final SimpMessagingTemplate  template;

    @Transactional
    public Message persist(Message m) { 
        return msgRepo.save(m);
    }

    @Transactional
    public void createStatuses(Message message, User from, User to) {
        save(from, message, MessageStatusType.DELIVERED);
        log.info(">>> Message {} status DELIVERED for {}", message.getMessageUuid(), from.getEmail());
        save(to, message, MessageStatusType.PENDING);
        log.info(">>> Message {} status PENDING for {}", message.getMessageUuid(), to.getEmail());
    }

    @Transactional
    public void save(User user, Message message, MessageStatusType statusType) {
        statusRepo.save(new MessageStatus(user, message, statusType));
    }

    public void broadcast(User from, User to, ChatMessageResponse dto) {
        template.convertAndSendToUser(from.getEmail(), "/queue/messages", dto);
        template.convertAndSendToUser(to.getEmail()  , "/queue/messages", dto);

        template.convertAndSendToUser(to.getEmail(), "/queue/notify",
              new NotificationDto(to.getEmail(), NotificationType.MESSAGE, from.getEmail()));
    }
}
