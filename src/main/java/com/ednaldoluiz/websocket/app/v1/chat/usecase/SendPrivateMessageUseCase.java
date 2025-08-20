// src/main/java/com/ednaldoluiz/websocket/app/v1/chat/usecase/SendPrivateMessageUseCase.java
package com.ednaldoluiz.websocket.app.v1.chat.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ednaldoluiz.websocket.app.v1.chat.command.SendMessageCommand;
import com.ednaldoluiz.websocket.app.v1.chat.dto.response.CipherPayload;
import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatRealtimeEnvelopeResponse;
import com.ednaldoluiz.websocket.app.v1.chat.dto.response.HistoryPayload;
import com.ednaldoluiz.websocket.domain.message.valueObject.HistoryContext;
import com.ednaldoluiz.websocket.domain.model.chat.Chat;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.domain.service.ChatDomainService;
import com.ednaldoluiz.websocket.domain.service.MessageDomainService;
import com.ednaldoluiz.websocket.infra.persistence.repository.UserRepository;
import com.ednaldoluiz.websocket.web.handler.exception.BusinessException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendPrivateMessageUseCase {

    private final ChatDomainService chatSvc;
    private final MessageDomainService msgSvc;
    private final UserRepository users;

    @Transactional
    public ChatRealtimeEnvelopeResponse execute(Long fromId, Long toUserId, SendMessageCommand cmd) {
        User from = users.findById(fromId)
                .orElseThrow(() -> new BusinessException("user.not-found"));
        User to = users.findById(toUserId)
                .orElseThrow(() -> new BusinessException("participant.not-found"));
        Chat chat = chatSvc.getOrCreatePrivateChat(from, to);

        // content obrigatório
        CipherPayload content = cmd.content();
        if (content == null) {
            throw new BusinessException("message.content.missing");
        }

        // senderCopy opcional -> fallback para o mesmo payload
        CipherPayload senderCopy = (cmd.senderCopy() != null) ? cmd.senderCopy() : content;

        byte[] bodyRecipient = content.decodeBodyBytes();
        byte[] bodySender    = senderCopy.decodeBodyBytes();
        int tRecipient       = content.type();
        int tSender          = senderCopy.type();

        HistoryPayload hist = cmd.history();
        HistoryContext history = HistoryContext.ofNullable(
                        hist.version(),
                        hist.decodeIvBytes(),
                        hist.decodeCiphertextBytes()
                  );

        log.info("[CMD] types(rec/snd)={}/{} len(rec/snd)={}/{}",
                tRecipient, tSender,
                bodyRecipient != null ? bodyRecipient.length : 0,
                bodySender    != null ? bodySender.length    : 0);

        return msgSvc.send(
                from, to, chat, cmd.messageUuid(), LocalDateTime.now(),
                bodyRecipient, tRecipient,
                bodySender,    tSender,
                history
        );
    }
}
