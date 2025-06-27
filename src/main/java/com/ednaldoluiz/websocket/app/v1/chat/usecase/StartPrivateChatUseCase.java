package com.ednaldoluiz.websocket.app.v1.chat.usecase;

import com.ednaldoluiz.websocket.web.websocket.event.ChatSummaryPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ednaldoluiz.websocket.app.v1.chat.dto.request.StartChatRequest;
import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatSummaryResponse;
import com.ednaldoluiz.websocket.domain.model.chat.Chat;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.domain.service.ChatDomainService;
import com.ednaldoluiz.websocket.infra.persistence.repository.ChatSummaryRepository;
import com.ednaldoluiz.websocket.infra.persistence.repository.UserRepository;
import com.ednaldoluiz.websocket.web.handler.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StartPrivateChatUseCase {

    private final ChatDomainService chatService;
    private final UserRepository userRepository;
    private final ChatSummaryRepository summaries;
    private final ChatSummaryPublisher publisher;

    @Transactional
    public ChatSummaryResponse execute(Long requesterId, StartChatRequest cmd) {

        if (requesterId.equals(cmd.participantId()))
            throw new BusinessException("Não é possível iniciar um chat consigo mesmo");

        User me = userRepository.findById(requesterId)
                .orElseThrow(() -> new BusinessException("Usuário não encontrado"));
        User you = userRepository.findById(cmd.participantId())
                .orElseThrow(() -> new BusinessException("Participante não encontrado"));

        Chat chat = chatService.getOrCreatePrivateChat(me, you);
        log.info("Chat privado {} criado para {}↔{}", chat.getId(), me.getId(), you.getId());

        summaries.upsertPair(
            me.getId(),   chat.getId(),
            you.getId(),  you.getName(), you.getAvatar(),
            me.getName(), me.getAvatar());

        // summaries.findByUserIdAndChatId(me.getId(),  chat.getId()).ifPresent(publisher::publish);
        // summaries.findByUserIdAndChatId(you.getId(), chat.getId()).ifPresent(publisher::publish);

        return summaries.findByUserIdAndChatId(me.getId(), chat.getId())
                        .map(ChatSummaryResponse::from)
                        .orElseThrow(() ->
                             new IllegalStateException("Resumo deveria existir agora"));
    }
}