package com.ednaldoluiz.websocket.app.v1.auth.validator;

public interface AuthValidator<T> {

    void validate(T request);
    
}