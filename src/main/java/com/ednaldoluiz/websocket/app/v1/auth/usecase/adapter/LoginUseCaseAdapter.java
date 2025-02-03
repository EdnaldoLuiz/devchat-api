package com.ednaldoluiz.websocket.app.v1.auth.usecase.adapter;

import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request.LoginRequest;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response.LoginResponse;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.port.LoginUseCasePort;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LoginUseCaseAdapter implements LoginUseCasePort {
    
    private final UserRepository userRepository;

    @Override
    public LoginResponse login(LoginRequest request) {


        return new LoginResponse(null, null);
    }
}
