package com.ednaldoluiz.websocket.app.v1.chat.usecase;

import java.util.Optional;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ednaldoluiz.websocket.app.v1.chat.dto.request.SendAttachmentMessageRequest;
import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatMessageResponse;
import com.ednaldoluiz.websocket.domain.model.chat.Chat;
import com.ednaldoluiz.websocket.domain.model.message.Message;
import com.ednaldoluiz.websocket.domain.model.message.MessageAttachments;
import com.ednaldoluiz.websocket.domain.model.message.MessageText;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.domain.service.ChatDomainService;
import com.ednaldoluiz.websocket.infra.persistence.repository.MessageAttachmentsRepository;
import com.ednaldoluiz.websocket.infra.persistence.repository.MessageRepository;
import com.ednaldoluiz.websocket.infra.persistence.repository.MessageTextRepository;
import com.ednaldoluiz.websocket.infra.persistence.repository.UserRepository;
import com.ednaldoluiz.websocket.web.websocket.store.AuthUser;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service 
@RequiredArgsConstructor
public class SendAttachmentMessageUseCase {

    private final ChatDomainService chatService;
    private final UserRepository    userRepo;
    private final MessageRepository msgRepo;
    private final MessageTextRepository messageTextRepository;
    private final MessageAttachmentsRepository attachmentRepository;
    private final SimpMessagingTemplate template;

    @Transactional
    public ChatMessageResponse execute(AuthUser auth, SendAttachmentMessageRequest request) {

        User from = userRepo.findById(auth.id()).orElseThrow();
        User to   = userRepo.findByEmail(request.toEmail()).orElseThrow();

        Chat chat = chatService.getOrCreatePrivateChat(from, to);

        log.info(">>> Chat {} criado ou recuperado para os Users {} e {}", chat.getId(), from.getEmail(), to.getEmail());

        Message msg = chatService.newMessage(chat, from, request.messageUuid());
        msgRepo.save(msg);

        log.info(">>> Mensagem {} criada para o Chat {}", msg.getMessageUuid(), chat.getId());

        MessageText.of(msg, request.content()).ifPresent(messageTextRepository::save);

        MessageAttachments attachment = new MessageAttachments(msg, request.attachmentType(), request.url());
        attachmentRepository.save(attachment);

        chatService.saveStatuses(msg, from, to);

        ChatMessageResponse out = ChatMessageResponse.from(
            msg,
            msg.getMessageText(),
            to.getId(),
            Optional.of(attachment)
        );

        chatService.broadcastAndNotify(from, to, out, template);
        return out;
    }
}
