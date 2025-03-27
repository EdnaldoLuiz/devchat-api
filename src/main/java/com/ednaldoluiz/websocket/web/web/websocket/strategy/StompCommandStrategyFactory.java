package com.ednaldoluiz.websocket.web.web.websocket.strategy;

import java.util.EnumMap;
import java.util.Map;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.stereotype.Component;

@Component
public class StompCommandStrategyFactory {

    private final Map<StompCommand, StompCommandStrategy> strategies = new EnumMap<>(StompCommand.class);

    public StompCommandStrategyFactory(
            ConnectCommandStrategy connectStrategy,
            DisconnectCommandStrategy disconnectStrategy,
            SubscribeCommandStrategy subscribeStrategy,
            UnsubscribeCommandStrategy unsubscribeStrategy,
            SendCommandStrategy sendCommandStrategy) {
        strategies.put(StompCommand.CONNECT, connectStrategy);
        strategies.put(StompCommand.DISCONNECT, disconnectStrategy);
        strategies.put(StompCommand.SUBSCRIBE, subscribeStrategy);
        strategies.put(StompCommand.UNSUBSCRIBE, unsubscribeStrategy);
        strategies.put(StompCommand.SEND, sendCommandStrategy);
    }

    public StompCommandStrategy getStrategy(StompCommand command) {
        if (!strategies.containsKey(command) || strategies.get(command) == null) {
            throw new IllegalArgumentException("No strategy found for command: " + command);
        }
        return strategies.get(command);
    }
}