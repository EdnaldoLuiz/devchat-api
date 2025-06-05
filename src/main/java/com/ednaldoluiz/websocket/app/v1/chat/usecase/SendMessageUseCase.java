package com.ednaldoluiz.websocket.app.v1.chat.usecase;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ednaldoluiz.websocket.app.v1.chat.command.AttachmentMessageCommand;
import com.ednaldoluiz.websocket.app.v1.chat.command.SendMessageCommand;
import com.ednaldoluiz.websocket.app.v1.chat.command.TextMessageCommand;
import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatMessageResponse;
import com.ednaldoluiz.websocket.domain.model.chat.Chat;
import com.ednaldoluiz.websocket.domain.model.message.Message;
import com.ednaldoluiz.websocket.domain.model.message.MessageFactory;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.domain.service.ChatDomainService;
import com.ednaldoluiz.websocket.domain.service.MessageDomainService;
import com.ednaldoluiz.websocket.infra.persistence.UserRepository;
import com.ednaldoluiz.websocket.web.websocket.store.AuthUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendMessageUseCase {

    private final ChatDomainService     chatSvc;
    private final MessageDomainService  msgSvc;
    private final UserRepository        userRepository;

    @Transactional
    public ChatMessageResponse execute(Long fromUserId, SendMessageCommand cmd, Long toUserId) {

        User from = userRepository.findById(fromUserId).orElseThrow();
        log.info(">>> User {} enviando mensagem para {}", from.getEmail(), toUserId);
        User to = userRepository.findById(toUserId)
            .orElseThrow(() -> new UsernameNotFoundException(""));

        Chat chat = chatSvc.getOrCreatePrivateChat(from, to);

        Message message = switch (cmd) {
            case TextMessageCommand txt           -> MessageFactory.withText(chat, from, txt.messageUuid(), txt.content());
            case AttachmentMessageCommand attach  -> MessageFactory.withAttachment(chat, from, attach.messageUuid(), attach.content(), attach.type(), attach.url());
        };

        msgSvc.persist(message);
        msgSvc.createStatuses(message, from, to);

        ChatMessageResponse dto = ChatMessageResponse.from(message, message.getMessageText(), to.getId());
        msgSvc.broadcast(from, to, dto);

        return dto;
    }
}

