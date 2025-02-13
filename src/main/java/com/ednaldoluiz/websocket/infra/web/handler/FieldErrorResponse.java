package com.ednaldoluiz.websocket.infra.web.handler;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FieldErrorResponse(String field, String message) {}