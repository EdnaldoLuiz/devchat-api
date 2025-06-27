package com.ednaldoluiz.websocket.web.websocket.event;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatSummaryResponse;
import com.ednaldoluiz.websocket.domain.model.chat.ChatSummary;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ChatSummaryPublisher {

    private final SimpMessagingTemplate broker;   // spring-websocket

    public void publish(ChatSummary summary) {
        var dto = ChatSummaryResponse.from(summary);
        // /user/queue/chats é privada para o usuário
        broker.convertAndSendToUser(
            summary.getUserId().toString(),
            "/queue/chats/summaries",
            dto
        );
    }
}
