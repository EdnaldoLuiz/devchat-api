package com.ednaldoluiz.websocket.web.controller.v1.chat;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.ednaldoluiz.websocket.app.v1.chat.dto.request.StartChatRequest;
import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatHistoryResponse;
import com.ednaldoluiz.websocket.app.v1.chat.dto.response.ChatSummaryResponse;
import com.ednaldoluiz.websocket.app.v1.chat.facade.ChatFacade;
import com.ednaldoluiz.websocket.app.v1.user.dto.response.UserSearchResponse;
import com.ednaldoluiz.websocket.app.v1.user.usecase.SearchUsersUseCase;
import com.ednaldoluiz.websocket.web.websocket.store.AuthUser;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ChatRestController {

    private final ChatFacade chatFacade;
    private final SearchUsersUseCase searchUsersUC;

    @GetMapping("/chats/{chatId}/messages")
    public ChatHistoryResponse listMessages(
            Authentication auth,
            @PathVariable Long chatId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        Long meId = ((AuthUser) auth.getPrincipal()).id();
        return chatFacade.listMessages(meId, chatId, page, size);
    }

    /* ------------------------ CHAT SUMMARIES ---------------------- */
    @GetMapping("/chats/summaries")
    public List<ChatSummaryResponse> listSummaries(Authentication auth) {
        Long meId = ((AuthUser) auth.getPrincipal()).id();
        return chatFacade.listSummaries(meId);
    }

    /* ------------------------ START PRIVATE CHAT ------------------ */
    @PostMapping("/chats/private")
    public ChatSummaryResponse startPrivateChat(
            Authentication auth,
            @RequestBody @Validated StartChatRequest request) {
        Long meId = ((AuthUser) auth.getPrincipal()).id();
        return chatFacade.startPrivate(meId, request);
    }
}
