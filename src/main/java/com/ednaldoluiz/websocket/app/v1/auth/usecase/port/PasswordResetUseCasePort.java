package com.ednaldoluiz.websocket.app.v1.auth.usecase.port;

public interface PasswordResetUseCasePort {

    void sendPasswordResetEmail(String recipientEmail, String resetToken);

    void resetPassword(String tokenValue, String newPassword);
    
}
