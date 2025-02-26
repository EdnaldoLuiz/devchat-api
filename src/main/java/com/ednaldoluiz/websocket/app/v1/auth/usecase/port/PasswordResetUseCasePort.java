package com.ednaldoluiz.websocket.app.v1.auth.usecase.port;

import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request.ResetPasswordRequest;

public interface PasswordResetUseCasePort {

    void sendPasswordResetEmail(String recipientEmail);

    void resetPassword(ResetPasswordRequest request);
    
}
