package com.ednaldoluiz.websocket.shared.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "signal")
public record SignalKeyProperties(

    int lowPreKeyThreshold,
    int signedPreKeyTtlDays,
    String checkSpkCron,
    String cleanupOrphansCron,
    int batchSize
    
) {}