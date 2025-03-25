package com.ednaldoluiz.websocket.v1.shared.helpers;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.ednaldoluiz.websocket.infra.web.handler.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ApiRequestHelper {

    private static final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    public static <T, R> ResponseEntity<?> doPost(
            String uri,
            WebTestClient client,
            R requestBody,
            Class<T> successType) {

        WebTestClient.ResponseSpec responseSpec = client.post()
                .uri(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .exchange();

        HttpStatusCode status = responseSpec.returnResult(String.class).getStatus();
        String responseBody = responseSpec.expectBody(String.class).returnResult().getResponseBody();

        if (responseBody == null) {
            return ResponseEntity.status(status).build();
        }

        try {
            if (status.is2xxSuccessful()) {
                T obj = objectMapper.readValue(responseBody, successType);
                return new ResponseEntity<>(obj, status);
            } else {
                ErrorResponse error = objectMapper.readValue(responseBody, ErrorResponse.class);
                return new ResponseEntity<>(error, status);
            }
        } catch (Exception e) {
            return ResponseEntity.status(status).body(responseBody);
        }
    }

    public static <T> ResponseEntity<?> doGet(String uri, WebTestClient client, Class<T> successType) {
        // Realiza a chamada e captura o resultado (status, headers e body)
        var result = client.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .returnResult(String.class);
        
        HttpStatusCode status = result.getStatus();
        var headers = result.getResponseHeaders();
        String responseBody = result.getResponseBody().blockFirst();
    
        // Se não houver corpo, retorna o ResponseEntity com os headers
        if (responseBody == null) {
            return new ResponseEntity<>(headers, status);
        }
        
        try {
            if (status.is2xxSuccessful()) {
                T obj = objectMapper.readValue(responseBody, successType);
                return new ResponseEntity<>(obj, headers, status);
            } else {
                ErrorResponse error = objectMapper.readValue(responseBody, ErrorResponse.class);
                return new ResponseEntity<>(error, headers, status);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(responseBody, headers, status);
        }
    }
    
}
