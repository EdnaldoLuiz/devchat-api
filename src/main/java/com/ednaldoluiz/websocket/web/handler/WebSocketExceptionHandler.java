package com.ednaldoluiz.websocket.web.handler;

import com.ednaldoluiz.websocket.web.handler.error.WebSocketErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class WebSocketExceptionHandler {

    @MessageExceptionHandler(Exception.class)
    @SendToUser("/queue/errors")
    public WebSocketErrorResponse handleException(Exception e) {
        String correlationId = MDC.get("correlationId");
        log.error("[WS ERROR][{}] Exception em handler WebSocket: {}", correlationId, e.getMessage(), e);
        return new WebSocketErrorResponse(
                "INTERNAL",
                "Ocorreu um erro inesperado. Tente novamente ou contate o suporte.",
                correlationId
        );
    }
}