package com.ednaldoluiz.websocket.infra.persistence.batch;

import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.ednaldoluiz.websocket.app.v1.chat.dto.response.BufferedMessage;
import com.ednaldoluiz.websocket.shared.config.MessageBatchProperties;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisMessageBufferService {

    private static final String LIST_KEY = "chat:messages";

    private final RedisTemplate<String, BufferedMessage> redis;
    private final MessageBatchProperties props;

    public void push(BufferedMessage msg) {
        redis.opsForList().rightPush(LIST_KEY, msg);
    }

    public List<BufferedMessage> pollBatch() {
        Long queueSizeObj = redis.opsForList().size(LIST_KEY);
        long queueSize = (queueSizeObj == null) ? 0 : queueSizeObj;
        if (queueSize == 0) {
            return List.of();
        }

        long fetchCount = Math.min(queueSize, props.batchSize());
        List<BufferedMessage> batch = redis.opsForList().leftPop(LIST_KEY, fetchCount);
        return (batch == null) ? List.of() : batch;
    }
}