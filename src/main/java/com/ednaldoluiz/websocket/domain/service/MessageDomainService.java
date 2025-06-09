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
import com.ednaldoluiz.websocket.infra.persistence.repository.MessageRepository;
import com.ednaldoluiz.websocket.infra.persistence.repository.MessageStatusRepository;
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
        save(from, message, MessageStatusType.SENT);
        log.info(">>> Message {} status DELIVERED for {}", message.getMessageUuid(), from.getId());
        save(to, message, MessageStatusType.DELIVERED);
        log.info(">>> Message {} status PENDING for {}", message.getMessageUuid(), to.getId());
    }

    @Transactional
    public void save(User user, Message message, MessageStatusType statusType) {
        statusRepo.save(new MessageStatus(user, message, statusType));
    }

    public void broadcast(User from, User to, ChatMessageResponse dto) {
        template.convertAndSendToUser(from.getId().toString(), "/queue/messages", dto);
        template.convertAndSendToUser(to.getId().toString()  , "/queue/messages", dto);

        template.convertAndSendToUser(to.getId().toString(), "/queue/notify",
            new NotificationDto(to.getId().toString(), NotificationType.MESSAGE, from.getId().toString()));
    }
}
