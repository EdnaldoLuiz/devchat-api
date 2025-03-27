package com.ednaldoluiz.websocket.web.controller.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericApiResponse {

    private final String message;

    @JsonCreator
    public GenericApiResponse(@JsonProperty("message") String message) {
        this.message = message;
    }
}