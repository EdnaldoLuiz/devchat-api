package com.ednaldoluiz.websocket.app.v1.chat.facade;

import org.springframework.stereotype.Service;

import com.ednaldoluiz.websocket.app.v1.chat.command.SendMessageCommand;
import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatHistoryResponse;
import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatMessageResponse;
import com.ednaldoluiz.websocket.app.v1.chat.usecase.ListChatMessageUseCase;
import com.ednaldoluiz.websocket.app.v1.chat.usecase.SendMessageUseCase;
import com.ednaldoluiz.websocket.web.websocket.store.AuthUser;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ChatFacade {

    private final SendMessageUseCase sendMessageUseCase;
    private final ListChatMessageUseCase listChatUC;

    public ChatMessageResponse send(Long fromUserId, SendMessageCommand cmd, Long toId) {
        return sendMessageUseCase.execute(fromUserId, cmd, toId);
    }

    public ChatHistoryResponse list(AuthUser auth, Long chatId, int page, int size) {
        return listChatUC.execute(auth, chatId, page, size);
    }
}
