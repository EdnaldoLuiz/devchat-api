// src/main/java/com/ednaldoluiz/websocket/app/v1/chat/usecase/ListChatMessagesUseCase.java
package com.ednaldoluiz.websocket.app.v1.chat.usecase;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatHistoryResponse;
import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatMessageResponse;
import com.ednaldoluiz.websocket.infra.persistence.repository.MessageRepository;
import com.ednaldoluiz.websocket.infra.persistence.repository.UsersChatsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ListChatMessagesUseCase {

    private final MessageRepository messageRepository;
    private final UsersChatsRepository usersChatsRepository;

    @Transactional(readOnly = true)
    public ChatHistoryResponse execute(Long meId, Long chatId, int page, int size) {
        log.info("[USECASE] ListChatMessages: meId={} chatId={} page={} size={}", meId, chatId, page, size);

        if (usersChatsRepository.findByChatIdAndUserId(chatId, meId).isEmpty()) {
            throw new IllegalStateException("Chat não encontrado ou acesso negado.");
        }

        Pageable pageable = PageRequest.of(page, size);
        Page<ChatMessageResponse> pageResult = messageRepository.findHistoryPage(chatId, meId, pageable);

        return new ChatHistoryResponse(
                chatId,
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.getContent(),
                null);
    }
}