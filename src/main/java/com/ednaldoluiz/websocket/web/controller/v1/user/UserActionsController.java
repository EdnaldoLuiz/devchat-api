package com.ednaldoluiz.websocket.web.controller.v1.user;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ednaldoluiz.websocket.app.v1.user.dto.request.SearchUsersRequest;
import com.ednaldoluiz.websocket.app.v1.user.dto.response.UserSearchResponse;
import com.ednaldoluiz.websocket.app.v1.user.usecase.SearchUsersUseCase;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@Validated
@RequiredArgsConstructor
public class UserActionsController {

    private final SearchUsersUseCase searchUsersUseCase;

    /**
     * GET /api/v1/users/search?q={query}&limit={limit}
     */
    @GetMapping("/search")
    @Operation(summary =  "Busca usuários por nome ou email",
            description = "Retorna uma lista de usuários que correspondem à consulta. Limite máximo de 50 resultados.")
    public ResponseEntity<List<UserSearchResponse>> searchUsers(
            @Valid @ModelAttribute SearchUsersRequest req
    ) {
        List<UserSearchResponse> results = searchUsersUseCase.execute(req);
        return ResponseEntity.ok(results);
    }
}
