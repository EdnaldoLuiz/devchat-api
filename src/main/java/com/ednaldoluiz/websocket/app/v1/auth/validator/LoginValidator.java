package com.ednaldoluiz.websocket.app.v1.auth.validator;

import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request.LoginRequest;

public interface LoginValidator {
    
    void validate(LoginRequest request);
    
}
