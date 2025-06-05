package com.ednaldoluiz.websocket.web.controller.v1.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class TypingController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.typing.{toUserId}")
    public void typing(
        @DestinationVariable String toUserId,
        @Payload Map<String,String> body
    ){
        messagingTemplate.convertAndSendToUser(toUserId, "/queue/typing", body);
    }

}
