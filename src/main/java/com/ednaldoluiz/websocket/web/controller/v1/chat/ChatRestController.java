package com.ednaldoluiz.websocket.web.controller.v1.chat;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ednaldoluiz.websocket.app.v1.chat.dto.request.ChangeChatStatusRequest;
import com.ednaldoluiz.websocket.app.v1.chat.dto.request.ChangeMessageStatusRequest;
import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatHistoryResponse;
import com.ednaldoluiz.websocket.app.v1.chat.facade.ChatFacade;
import com.ednaldoluiz.websocket.web.websocket.store.AuthUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/chats")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatFacade chatFacade;

    @GetMapping("/{chatId}/messages")
    public ChatHistoryResponse list(
            @PathVariable Long chatId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            Authentication auth
    ) {
        return chatFacade.list(
            (AuthUser) auth.getPrincipal(), chatId, page, size
        );
    }

    // @PatchMapping("/{chatId}")
    // public void changeStatus(
    //         @PathVariable Long chatId,
    //         @RequestBody ChangeChatStatusRequest req,
    //         Authentication auth
    // ) {
    //     chatFacade.changeChatStatus(
    //         (AuthUser) auth.getPrincipal(), req.withChatId(chatId)
    //     );
    // }

    // @PatchMapping("/messages/{messageId}")
    // public void changeMessageStatus(
    //         @PathVariable Long messageId,
    //         @RequestBody ChangeMessageStatusRequest req,
    //         Authentication auth
    // ) {
    //     chatFacade.changeMessageStatus(
    //         (AuthUser) auth.getPrincipal(), req.withMessageId(messageId)
    //     );
    // }
}
