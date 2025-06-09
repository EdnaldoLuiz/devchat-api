package com.ednaldoluiz.websocket.app.v1.chat.facade;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ednaldoluiz.websocket.app.v1.chat.command.SendMessageCommand;
import com.ednaldoluiz.websocket.app.v1.chat.dto.request.StartChatRequest;
import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatHistoryResponse;
import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatMessageResponse;
import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatSummaryResponse;
import com.ednaldoluiz.websocket.app.v1.chat.usecase.ListChatMessagesUseCase;
import com.ednaldoluiz.websocket.app.v1.chat.usecase.ListChatSummariesUseCase;
import com.ednaldoluiz.websocket.app.v1.chat.usecase.SendMessageUseCase;
import com.ednaldoluiz.websocket.app.v1.chat.usecase.StartPrivateChatUseCase;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatFacade {

    private final SendMessageUseCase sendMessageUseCase;
    private final ListChatSummariesUseCase listSummariesUseCase;
    private final StartPrivateChatUseCase startPrivateChatUseCase;
    private final ListChatMessagesUseCase listChatMessagesUseCase;

    public ChatMessageResponse send(Long fromUserId, SendMessageCommand cmd, Long toId) {
        return sendMessageUseCase.execute(fromUserId, cmd, toId);
    }

    public ChatSummaryResponse startPrivate(Long meId, StartChatRequest req) {
        return startPrivateChatUseCase.execute(meId, req);
    }

    public List<ChatSummaryResponse> listSummaries(Long userId) {
        return listSummariesUseCase.execute(userId);
    }

    public ChatHistoryResponse listMessages(Long meId, Long chatId, int page, int size) {
        return listChatMessagesUseCase.execute(meId, chatId, page, size);
    }
}
