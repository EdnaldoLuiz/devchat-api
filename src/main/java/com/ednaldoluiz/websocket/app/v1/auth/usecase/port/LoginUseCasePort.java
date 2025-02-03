package com.ednaldoluiz.websocket.app.v1.auth.usecase.port;

import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request.LoginRequest;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response.LoginResponse;

public interface LoginUseCasePort {
    
    LoginResponse login(LoginRequest request);
    
}
