package com.ednaldoluiz.websocket.v1.shared.helpers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;

import com.ednaldoluiz.websocket.infra.web.handler.ErrorResponse;

public class ApiRequestHelper {

    public static <T, R> ResponseEntity<?> doPost(String uri, WebClient webClient, R requestBody, Class<T> responseType) {
        return webClient.post()
                .uri(uri)
                .bodyValue(requestBody)
                .exchangeToMono(clientResponse -> {
                    if (clientResponse.statusCode().is2xxSuccessful()) {
                        return clientResponse.bodyToMono(responseType)
                                .map(body -> new ResponseEntity<>(body, clientResponse.statusCode()));
                    } else {
                        return clientResponse.bodyToMono(ErrorResponse.class)
                                .map(body -> new ResponseEntity<>(body, clientResponse.statusCode()));
                    }
                })
                .block();
    }
}