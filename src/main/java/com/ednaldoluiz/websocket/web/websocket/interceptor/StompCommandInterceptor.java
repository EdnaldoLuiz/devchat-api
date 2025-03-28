package com.ednaldoluiz.websocket.web.websocket.interceptor;

import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.web.websocket.strategy.StompCommandStrategy;
import com.ednaldoluiz.websocket.web.websocket.strategy.StompCommandStrategyFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompCommandInterceptor implements ChannelInterceptor {

    private final StompCommandStrategyFactory strategyFactory;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        // Extrai o accessor para acessar o comando e os headers
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (accessor == null) {
            return message;
        }
        // Obtém o comando STOMP da mensagem
        StompCommand command = accessor.getCommand();
        if (command != null) {
            log.info("Intercepted STOMP command: {}", command);
            // Obtém a estratégia correspondente ao comando
            StompCommandStrategy strategy = strategyFactory.getStrategy(command);
            strategy.handle(accessor);
        }
        return message;
    }
}
