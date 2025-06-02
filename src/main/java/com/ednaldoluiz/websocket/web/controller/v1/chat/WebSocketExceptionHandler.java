package com.ednaldoluiz.websocket.web.controller.v1.chat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class WebSocketExceptionHandler {

    @MessageExceptionHandler(Exception.class)
    @SendToUser("/queue/errors")
    public String handleException(Exception e) {
        log.error("[WS ERROR] Exception em handler WebSocket: {}", e.getMessage(), e);
        return "Erro no socket: " + e.getMessage();
    }
}