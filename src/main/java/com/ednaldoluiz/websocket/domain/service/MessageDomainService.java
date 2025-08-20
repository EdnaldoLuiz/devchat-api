// src/main/java/com/ednaldoluiz/websocket/domain/service/MessageDomainService.java
package com.ednaldoluiz.websocket.domain.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatRealtimeEnvelopeResponse;
import com.ednaldoluiz.websocket.domain.message.valueObject.HistoryContext;
import com.ednaldoluiz.websocket.domain.model.chat.Chat;
import com.ednaldoluiz.websocket.domain.model.message.CipherType;
import com.ednaldoluiz.websocket.domain.model.message.Message;
import com.ednaldoluiz.websocket.domain.model.message.MessageCopy;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.repository.MessageCopyRepository;
import com.ednaldoluiz.websocket.infra.persistence.repository.MessageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageDomainService {

    private final MessageRepository messageRepository;
    private final MessageCopyRepository copyRepository;
    private final SimpMessagingTemplate broker;

    @Transactional
    public ChatRealtimeEnvelopeResponse send(
            User from, User to, Chat chat, UUID messageUuid, LocalDateTime sentAt,
            byte[] bodyRecipient, int typeRecipient,
            byte[] bodySender, int typeSender,
            HistoryContext history
    ) {

        Objects.requireNonNull(bodyRecipient, "cipher body recipient");
        Objects.requireNonNull(bodySender, "cipher body sender");
        Objects.requireNonNull(history, "history context");

        // 1) cria mensagem + histórico (algorithm/version vindos do VO com defaults seguros)
        Message message = Message.create(chat, from, messageUuid, sentAt, history);
        messageRepository.persist(message);

        // 2) cria cópias
        MessageCopy copyTo = MessageCopy.of(message, chat, to,   CipherType.from((short) typeRecipient), bodyRecipient);
        MessageCopy copyMe = MessageCopy.of(message, chat, from, CipherType.from((short) typeSender),    bodySender);

        copyRepository.persistAll(List.of(copyTo, copyMe));

        // 3) envelopes WS
        ChatRealtimeEnvelopeResponse dtoTo = ChatRealtimeEnvelopeResponse.fromRaw(
                message.getId(), message.getMessageUuid(),
                from.getId(), to.getId(),
                typeRecipient, bodyRecipient,
                null, message.getSentAt());

        ChatRealtimeEnvelopeResponse dtoMe = ChatRealtimeEnvelopeResponse.fromRaw(
                message.getId(), message.getMessageUuid(),
                from.getId(), from.getId(),
                typeSender, bodySender,
                null, message.getSentAt());

        broker.convertAndSendToUser(to.getId().toString(),   "/queue/messages", dtoTo);
        broker.convertAndSendToUser(from.getId().toString(), "/queue/messages", dtoMe);

        log.info("[SEND] uuid={} chat={} from={} to={} bytes(rec/snd)={}/{}",
                messageUuid, chat.getId(), from.getId(), to.getId(),
                bodyRecipient.length, bodySender.length);

        return dtoMe;
    }
}
