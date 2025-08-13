package com.ednaldoluiz.websocket.infra.persistence.batch;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ednaldoluiz.websocket.app.v1.chat.dto.response.BufferedMessage;
import com.ednaldoluiz.websocket.domain.event.MessageCreatedEvent;
import com.ednaldoluiz.websocket.domain.model.chat.Chat;
import com.ednaldoluiz.websocket.domain.model.message.CipherType;
import com.ednaldoluiz.websocket.domain.model.message.Message;
import com.ednaldoluiz.websocket.domain.model.message.MessageAttachments;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.repository.ChatRepository;
import com.ednaldoluiz.websocket.infra.persistence.repository.MessageRepository;
import com.ednaldoluiz.websocket.infra.persistence.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageBatchFlusher {

    private final RedisMessageBufferService buffer;
    private final MessageRepository messageRepository;
    private final ApplicationEventPublisher events;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;

    @Transactional
    @Scheduled(fixedDelayString = "${app.batch.flush-interval:1000}")
    public void flush() {
        List<BufferedMessage> batch = buffer.pollBatch();
        if (batch.isEmpty())
            return;

        List<Message> entities = batch.stream()
                .map(this::toEntity)
                .toList();

        messageRepository.persistAll(entities);

        entities.forEach(message -> events.publishEvent(
                new MessageCreatedEvent(
                        message.getId(),
                        message.getChat().getId(),
                        message.getUser().getId(),
                        message.getSentAt(),
                        "[CIPHER]")));

        log.debug("💾 Flush {} mensagens para DB", entities.size());
    }

    private Message toEntity(BufferedMessage bm) {
        log.info("[FLUSH->toEntity] uuid={} cipherType={} chat={} from={}",
        bm.messageUuid(), bm.cipherType(), bm.chatId(), bm.fromId());
        Chat chat = chatRepository.getReferenceById(bm.chatId());
        User from = userRepository.getReferenceById(bm.fromId());

        byte[] body = java.util.Base64.getDecoder().decode(bm.cipherBodyB64()); // <-- nome novo

        Message message = new Message(
                bm.messageUuid(),
                chat,
                from,
                body);

        // **importante**: persistir o tipo de cifra
        // se seu enum tem from(short), pode fazer cast; ou crie um fromCode(int).
        message.setCipherType(CipherType.from((short) bm.cipherType()));

        if (bm.hasAttachment()) {
            message.addAttachment(new MessageAttachments(
                    message,
                    bm.attachmentType(),
                    bm.attachmentUrl()));
        }

        message.setSentAt(
                LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(bm.sentAtMillis()),
                        ZoneOffset.UTC));
        return message;
    }
}
