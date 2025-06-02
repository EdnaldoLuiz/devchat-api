package com.ednaldoluiz.websocket.app.v1.chat.usecase;

import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatHistoryResponse;
import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatMessageResponse;
import com.ednaldoluiz.websocket.domain.model.chat.ChatStatus;
import com.ednaldoluiz.websocket.domain.model.chat.UsersChat;
import com.ednaldoluiz.websocket.domain.model.message.Message;
import com.ednaldoluiz.websocket.infra.persistence.MessageRepository;
import com.ednaldoluiz.websocket.infra.persistence.UsersChatsRepository;
import com.ednaldoluiz.websocket.web.websocket.store.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListChatMessageUseCase {

    private final UsersChatsRepository  usersChatsRepo;
    private final MessageRepository     messageRepo;

    @Transactional(readOnly = true)
    public ChatHistoryResponse execute(AuthUser auth, Long chatId, int page, int size) {

        UsersChat link = usersChatsRepo
                .findByChatIdAndUserId(chatId, auth.id())
                .orElseThrow(() -> new IllegalStateException("Chat não encontrado ou acesso negado"));

        if (link.getStatus() == ChatStatus.BLOCKED) {
            throw new IllegalStateException("Chat está bloqueado para este usuário");
        }

        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "sentAt");

        Page<Message> paged = messageRepo.findByChatIdFetchText(chatId, pageable);

        List<ChatMessageResponse> dtoList = paged.stream()
            .map(m -> {
                String recipient = getOtherEmail(chatId, auth.id());
                return ChatMessageResponse.from(m, m.getMessageText(), recipient);
            })
            .toList();

        log.debug("HISTORY chat:{} user:{} page:{}/{}", chatId, auth.email(), page, paged.getTotalPages());

        return new ChatHistoryResponse(
                chatId,
                page,
                size,
                paged.getTotalElements(),
                dtoList
        );
    }

    private String getOtherEmail(Long chatId, Long myUserId) {
    List<UsersChat> participantes = usersChatsRepo.findAllByChatId(chatId);
    if (participantes.size() != 2) {
        throw new IllegalStateException("Chat privado deve ter exatamente 2 participantes");
    }

    return participantes.stream()
        .map(uc -> uc.getUser())
        .filter(user -> !user.getId().equals(myUserId))
        .findFirst()
        .orElseThrow(() -> new IllegalStateException("Outro participante não encontrado"))
        .getEmail();
}

}
