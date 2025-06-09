// src/main/java/com/ednaldoluiz/websocket/app/v1/chat/usecase/ListChatMessagesUseCase.java
package com.ednaldoluiz.websocket.app.v1.chat.usecase;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatHistoryResponse;
import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatMessageResponse;
import com.ednaldoluiz.websocket.infra.persistence.repository.MessageRepository;
import com.ednaldoluiz.websocket.infra.persistence.repository.UsersChatsRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ListChatMessagesUseCase {

    private final MessageRepository    messageRepository;
    private final UsersChatsRepository usersChatsRepository;

    @Transactional(readOnly = true)
    public ChatHistoryResponse execute(
            Long meId,
            Long chatId,
            int page,
            int size
    ) {
        // 1) valida acesso
        if (usersChatsRepository
                .findByChatIdAndUserId(chatId, meId)
                .isEmpty()) {
            throw new IllegalStateException("Chat não encontrado ou acesso negado.");
        }

        // 2) consulta página de mensagens
        Pageable pageable = PageRequest.of(page, size);
        Page<ChatMessageResponse> pageResult = messageRepository.findRecentMessages(chatId, meId, pageable);

        // 3) embala num DTO de histórico
        return new ChatHistoryResponse(
            chatId,
            pageResult.getNumber(),
            pageResult.getSize(),
            pageResult.getTotalElements(),
            pageResult.getContent()
        );
    }
}
