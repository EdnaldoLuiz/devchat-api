package com.ednaldoluiz.websocket.shared.constants;

public interface BeanConstants {

    String SNOWFLAKE_ID_GENERATOR = "snowflakeIdGenerator";
    String SWAGGER_OPEN_API = "customOpenAPI";

    interface Security {
        String PASSWORD_ENCODER = "passwordEncoder";
        String SECURITY_FILTER_CHAIN = "securityFilterChain";
        String AUTHENTICATION_MANAGER = "authenticationManager";
    }
}
