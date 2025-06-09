package com.ednaldoluiz.websocket.app.v1.chat.usecase;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatSummaryResponse;
import com.ednaldoluiz.websocket.infra.persistence.jdbcrepository.ChatSummaryJdbcRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class ListChatSummariesUseCase {

    private final ChatSummaryJdbcRepository chatSummaryJdbcRepository;

    public List<ChatSummaryResponse> execute(Long userId) {
        log.info("Listando chat summaries para usuário {}", userId);

        return chatSummaryJdbcRepository.findAllByUserId(userId)
            .stream()
            .map(ChatSummaryResponse::from)
            .toList();
    }
}
