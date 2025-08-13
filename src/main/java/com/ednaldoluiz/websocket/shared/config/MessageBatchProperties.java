package com.ednaldoluiz.websocket.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.batch")
public record MessageBatchProperties(
        int batchSize,
        long flushInterval
) {}