package com.ednaldoluiz.websocket.web.websocket.interceptor;

import org.springframework.lang.NonNull;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class CipherSizeLimiterInterceptor implements ChannelInterceptor {

    private static final int MAX_BYTES = 130_000; // ~100 KB + overhead

    @Override
    public Message<?> preSend(@NonNull Message<?> msg, @NonNull MessageChannel ch) {
        StompHeaderAccessor acc = MessageHeaderAccessor.getAccessor(msg, StompHeaderAccessor.class);
        if (acc != null
                && StompCommand.SEND.equals(acc.getCommand())
                && msg.getPayload() instanceof byte[] body
                && body.length > MAX_BYTES) {
            throw new MessagingException("Ciphertext muito grande: "
                    + body.length + " bytes, máximo permitido: " + MAX_BYTES + " bytes");
        }
        return msg;
    }
}
