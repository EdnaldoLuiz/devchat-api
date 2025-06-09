package com.ednaldoluiz.websocket.app.v1.chat.usecase;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ednaldoluiz.websocket.app.v1.chat.dto.request.StartChatRequest;
import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatSummaryResponse;
import com.ednaldoluiz.websocket.domain.model.chat.Chat;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.domain.service.ChatDomainService;
import com.ednaldoluiz.websocket.infra.persistence.jdbcrepository.ChatSummaryJdbcRepository;
import com.ednaldoluiz.websocket.infra.persistence.jdbcrepository.projection.ChatSummaryProjection;
import com.ednaldoluiz.websocket.infra.persistence.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartPrivateChatUseCase {

    private final ChatDomainService chatSvc;
    private final ChatSummaryJdbcRepository summaryRepo;
    private final UserRepository userRepo;

    @Transactional
    public ChatSummaryResponse execute(Long meId, StartChatRequest req) {

        if (meId.equals(req.participantId())) {
            throw new IllegalArgumentException("Não é permitido conversar consigo mesmo.");
        }

        User me  = userRepo.findById(meId)
                           .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
        User you = userRepo.findById(req.participantId())
                           .orElseThrow(() -> new UsernameNotFoundException("Destino não encontrado"));

        Chat chat = chatSvc.getOrCreatePrivateChat(me, you);

        /* Agora procura a linha do resumo. Se ainda não existir, criamos manualmente 
           (fallback de segurança caso trigger falhe).                               */
        return summaryRepo.findByUserIdAndChatId(meId, chat.getId())
                .map(ChatSummaryResponse::from)
                .orElseGet(() -> {
                    ChatSummaryProjection p = new ChatSummaryProjection(
                        chat.getId(),                // chatId
                        you.getId(),                 // participantId
                        null,                        // roomId
                        null, null,                  // lastMsg*
                        you.getName(),
                        you.getAvatar(),
                        null,                        // lastMessageAt
                        0                            // unread
                    );
                    return ChatSummaryResponse.from(p);
                });
    }
}
