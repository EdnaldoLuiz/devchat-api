package com.ednaldoluiz.websocket.web.controller.v1.chat;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ednaldoluiz.websocket.app.v1.chat.command.SendMessageCommand;
import com.ednaldoluiz.websocket.app.v1.chat.facade.ChatFacade;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/ws")
public class MessageWsController {

    private final ChatFacade chatFacade;

    @MessageMapping("/chat.private.{toUserId}")
    public void privateMsg(
            @DestinationVariable Long toUserId,
            @Payload SendMessageCommand cmd,
            Principal principal
    ) {
        log.info(">>> SendMessageCommand: {}", cmd);
        Long fromUserId = Long.valueOf(principal.getName());
        log.info(">>> User {} enviando mensagem privada para {}", fromUserId, toUserId);
        chatFacade.send(fromUserId, cmd, toUserId);
    }
}