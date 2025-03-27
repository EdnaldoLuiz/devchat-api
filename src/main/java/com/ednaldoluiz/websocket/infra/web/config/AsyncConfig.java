package com.ednaldoluiz.websocket.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import lombok.experimental.FieldDefaults;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class AsyncConfig {

    static int CORES = Runtime.getRuntime().availableProcessors();
    static int THREADS_MINIMAS = Math.max(2, CORES / 2);
    static int THREADS_MAXIMAS = CORES * 2;
    static int QUEUE_CAPACITY = 50;

    @Bean(name = "customTaskExecutor")
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(THREADS_MINIMAS);  
        executor.setMaxPoolSize(THREADS_MAXIMAS);
        executor.setQueueCapacity(QUEUE_CAPACITY);
        executor.setThreadNamePrefix("AsyncProcessor-");
        executor.setAllowCoreThreadTimeOut(true);
        executor.setKeepAliveSeconds(30);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}