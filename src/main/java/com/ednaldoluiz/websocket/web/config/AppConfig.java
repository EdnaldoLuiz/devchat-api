package com.ednaldoluiz.websocket.web.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.ednaldoluiz.websocket.shared.config.MessageBatchProperties;
import com.ednaldoluiz.websocket.shared.config.SignalKeyProperties;

@Configuration
@EnableConfigurationProperties({
    SignalKeyProperties.class,
    MessageBatchProperties.class
})
public class AppConfig {}