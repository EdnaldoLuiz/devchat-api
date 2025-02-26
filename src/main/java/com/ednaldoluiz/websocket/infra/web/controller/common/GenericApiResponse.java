package com.ednaldoluiz.websocket.infra.web.controller.common;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericApiResponse {

    private final String message;

    public GenericApiResponse(String message) {
        this.message = message;
    }
}