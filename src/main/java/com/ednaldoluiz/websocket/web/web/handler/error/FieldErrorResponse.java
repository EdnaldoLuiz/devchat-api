package com.ednaldoluiz.websocket.web.web.handler.error;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record FieldErrorResponse(String field, String message) {}