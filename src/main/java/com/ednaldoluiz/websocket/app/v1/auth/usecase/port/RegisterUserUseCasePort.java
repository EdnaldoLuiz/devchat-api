package com.ednaldoluiz.websocket.app.v1.auth.usecase.port;

import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response.RegisterResponse;

public interface RegisterUserUseCasePort {
    
    RegisterResponse register(RegisterRequest request);

}
