package com.ednaldoluiz.websocket.app.v1.auth.validator;

import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request.RegisterRequest;

public interface RegisterValidator {
    /**
     * Valida o request e lança uma exceção se a validação falhar.
     * @param request o RegisterRequest a ser validado.
     * @throws IllegalArgumentException caso a validação não seja satisfeita.
     */
    void validate(RegisterRequest request);

}
