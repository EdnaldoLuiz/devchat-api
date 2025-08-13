package com.ednaldoluiz.websocket.infra.persistence.jdbcrepository.projection;

import java.time.Instant;

public record SignalHandshakeBundleProjection(
    int registrationId,
    byte[] identityKey,
    int signedPreKeyId,
    byte[] signedPreKey,
    byte[] signedPreKeySig,
    Instant createdAt
) {}