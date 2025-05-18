package com.ednaldoluiz.websocket.web.config;

import com.ednaldoluiz.websocket.web.websocket.interceptor.JwtChannelInterceptor;
import com.ednaldoluiz.websocket.web.websocket.interceptor.JwtHandshakeInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

import com.ednaldoluiz.websocket.web.websocket.interceptor.StompCommandInterceptor;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    final StompCommandInterceptor stompCommandInterceptor;
    final JwtHandshakeInterceptor jwtHandshakeInterceptor;
    final JwtChannelInterceptor jwtChannelInterceptor;

    @Value("${app.cors.allowed-origins}")
    private String[] allowed;

    static long HEARTBEAT = 10_000;
    static int MESSAGE_SIZE_LIMIT = 1024 * 1024;
    static int SEND_BUFFER_SIZE_LIMIT = 1024 * 1024;
    static int SEND_TIME_LIMIT = 20_000;

    /**
     * Registra o endpoint que os clientes usarão para se conectar via WebSocket.
     * Aqui, também podemos definir um handshake interceptor para validar tokens
     * JWT, se necessário.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOrigins(allowed)
                .addInterceptors(jwtHandshakeInterceptor)
                .withSockJS()
                    .setSessionCookieNeeded(false);
    }

    /**
     * Configura o transporte WebSocket. Podemos definir o tamanho máximo de
     * mensagens,
     * o tamanho máximo do buffer de envio e o tempo limite de envio.
     */

    @Override
    public void configureWebSocketTransport(@NonNull WebSocketTransportRegistration registration) {
        registration
                .setMessageSizeLimit(MESSAGE_SIZE_LIMIT)
                .setSendBufferSizeLimit(SEND_BUFFER_SIZE_LIMIT)
                .setSendTimeLimit(SEND_TIME_LIMIT);
    }

    /**
     * Configura o message broker. Podemos definir um broker simples para roteamento
     * de mensagens
     * e também o prefixo para mensagens enviadas para métodos anotados
     * com @MessageMapping.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config
                .setApplicationDestinationPrefixes("/app")
                .setUserDestinationPrefix("/user")
                .enableSimpleBroker("/topic", "/queue")
                .setTaskScheduler(heartBeatScheduler())
                .setHeartbeatValue(new long[]{HEARTBEAT, HEARTBEAT});
    }


    /**
     * Configura o canal de entrada do cliente. Aqui, podemos adicionar interceptadores
     * para processar mensagens antes de serem enviadas para os controladores.
     */

    @Override
    public void configureClientInboundChannel(@NonNull ChannelRegistration registration) {
        registration.interceptors(stompCommandInterceptor, jwtChannelInterceptor);
    }

    @Bean
    public TaskScheduler heartBeatScheduler() {
        return new ThreadPoolTaskScheduler();
    }
}