package com.ednaldoluiz.websocket.infra.web.controller.v1;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ednaldoluiz.websocket.app.v1.auth.validator.chat.usecase.port.SendMessageUseCasePort;
import com.ednaldoluiz.websocket.app.v1.auth.validator.chat.usecase.port.StartPrivateChatUseCasePort;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatController {

    private final StartPrivateChatUseCasePort startPrivateChatUseCasePort;
    private final SendMessageUseCasePort sendMessageUseCasePort;

    @PostMapping("/start")
    public ResponseEntity<Long> startPrivateChat(@RequestParam Long userId1, @RequestParam Long userId2) {
        Long chatId = startPrivateChatUseCasePort.startPrivateChat(userId1, userId2);
        return ResponseEntity.ok(chatId);
    }

    @PostMapping("/{chatId}/messages")
    public ResponseEntity<Void> sendMessage(@PathVariable Long chatId,
                                            @RequestParam Long senderId,
                                            @RequestParam String content) {
        sendMessageUseCasePort.sendMessage(chatId, senderId, content);
        return ResponseEntity.ok().build();
    }
}