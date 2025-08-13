package com.ednaldoluiz.websocket.domain.service;

import org.springframework.data.redis.RedisSystemException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ednaldoluiz.websocket.app.v1.chat.command.SendMessageCommand;
import com.ednaldoluiz.websocket.app.v1.chat.dto.response.BufferedMessage;
import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatMessageResponse;
import com.ednaldoluiz.websocket.domain.model.chat.Chat;
import com.ednaldoluiz.websocket.domain.model.message.Message;
import com.ednaldoluiz.websocket.domain.model.message.MessageFactory;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.batch.RedisMessageBufferService;
import com.ednaldoluiz.websocket.infra.persistence.repository.MessageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageDomainService {

    private final RedisMessageBufferService bufferService;
    private final MessageRepository         messageRepository;
    private final SimpMessagingTemplate     broker;

    /**
     * Orquestra o envio de mensagem: joga em Redis buffer, faz broadcast.
     * Fallback: salva direto no DB se Redis indisponível.
     */
    @Transactional(noRollbackFor = RedisSystemException.class)
    public ChatMessageResponse send(User from, User to, Chat chat, SendMessageCommand cmd, byte[] cipherBody) {

        BufferedMessage buffered = BufferedMessage.of(from, to, chat, cmd, cipherBody);
        log.info("[SEND] uuid={} cipherType={} from={} to={} chat={}",
        buffered.messageUuid(), buffered.cipherType(), from.getId(), to.getId(), chat.getId());

        boolean bufferedOk = false;
        try {
            bufferService.push(buffered);
            bufferedOk = true;
        } catch (RedisSystemException ex) {
            log.error("Redis indisponível – fallback para DB direto", ex);
            Message entity = MessageFactory.fromBuffered(buffered, chat, from, to);
            messageRepository.persist(entity);
        }

        ChatMessageResponse response = ChatMessageResponse.from(buffered, to.getId(), !bufferedOk);
        broker.convertAndSendToUser(from.getId().toString(), "/queue/messages", response);
        broker.convertAndSendToUser(to.getId().toString(),   "/queue/messages", response);

        log.info("Mensagem {} enfileirada (buffer Redis? {}), from={} to={} chat={}", 
                 buffered.messageUuid(), bufferedOk, from.getId(), to.getId(), chat.getId());

        return response;
    }
}
