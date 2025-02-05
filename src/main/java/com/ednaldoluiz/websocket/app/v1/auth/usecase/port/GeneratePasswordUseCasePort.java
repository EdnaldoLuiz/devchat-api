package com.ednaldoluiz.websocket.app.v1.auth.usecase.port;

import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response.GeneratedPasswordResponse;

public interface GeneratePasswordUseCasePort {

    GeneratedPasswordResponse  generateStrongPassword();
    
}
