package com.ednaldoluiz.websocket.app.v1.chat.usecase;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ednaldoluiz.websocket.app.v1.chat.command.AttachmentMessageCommand;
import com.ednaldoluiz.websocket.app.v1.chat.command.SendMessageCommand;
import com.ednaldoluiz.websocket.app.v1.chat.command.TextMessageCommand;
import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatMessageResponse;
import com.ednaldoluiz.websocket.domain.model.chat.Chat;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.domain.service.ChatDomainService;
import com.ednaldoluiz.websocket.domain.service.MessageDomainService;
import com.ednaldoluiz.websocket.infra.persistence.repository.UserRepository;
import com.ednaldoluiz.websocket.web.handler.exception.BusinessException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SendPrivateMessageUseCase {

    private final ChatDomainService chatSvc;
    private final MessageDomainService msgSvc;
    private final UserRepository users;

    @Transactional
    public ChatMessageResponse execute(Long fromId, Long toUserId, SendMessageCommand cmd) {

        User from = users.findById(fromId)
                .orElseThrow(() -> new BusinessException("user.not-found"));
        User to = users.findById(toUserId)
                .orElseThrow(() -> new BusinessException("participant.not-found"));

        Chat chat = chatSvc.getOrCreatePrivateChat(from, to);

        byte[] cipher = switch (cmd) {
            case TextMessageCommand textMessageCommand             -> textMessageCommand.content().decodeBody();
            case AttachmentMessageCommand attachmentMessageCommand -> attachmentMessageCommand.content().decodeBody();
        };

        return msgSvc.send(from, to, chat, cmd, cipher);
    }
}
