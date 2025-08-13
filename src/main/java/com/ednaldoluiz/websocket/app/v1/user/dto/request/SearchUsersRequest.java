package com.ednaldoluiz.websocket.app.v1.user.dto.request;

import jakarta.validation.constraints.NotBlank;

public record SearchUsersRequest(

    @NotBlank(message = "O parâmetro 'query' não pode ser vazio")
    String query

) {}
