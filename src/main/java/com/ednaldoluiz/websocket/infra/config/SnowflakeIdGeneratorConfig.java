package com.ednaldoluiz.websocket.infra.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ednaldoluiz.websocket.shared.constants.BeanConstants;
import com.ednaldoluiz.websocket.shared.generator.SnowflakeIdGenerator;

@Configuration
public class SnowflakeIdGeneratorConfig {

    @Bean(name = BeanConstants.SNOWFLAKE_ID_GENERATOR)
    public SnowflakeIdGenerator snowflakeIdGenerator(
            @Value("${snowflake.datacenter-id}") long datacenterId,
            @Value("${snowflake.machine-id}") long machineId) {
        return new SnowflakeIdGenerator(datacenterId, machineId);
    }
}