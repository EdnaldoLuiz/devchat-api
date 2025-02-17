package com.ednaldoluiz.websocket.infra.web.controller.v1.auth;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response.RegisterResponse;
import com.ednaldoluiz.websocket.infra.web.handler.ErrorResponse;
import com.ednaldoluiz.websocket.infra.web.route.Paths;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RequestMapping(
    path = Paths.V1.Auth.AUTH, 
    produces = MediaType.APPLICATION_JSON_VALUE
)
@Tag(name = "Autenticação", description = "Endpoints responsáveis pelo registro e autenticação de usuários.")
public interface AuthApi {

    /**
     * Endpoint para registrar um novo usuário.
     *
     * @param request Objeto contendo os dados necessários para o registro do usuário.
     * @return Objeto contendo ID, email, token e refresh token do usuário criado.
     */
    @PostMapping(Paths.Auth.REGISTER)
    @Operation(
        summary = AuthDocs.Register.SUMMARY, 
        description = AuthDocs.Register.DESCRIPTION
    )
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso.", content = {
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, 
                schema = @Schema(implementation = RegisterResponse.class),
                examples = @ExampleObject(value = AuthDocs.Register.STATUS_201_RESPONSE)
            )
        }),
        @ApiResponse(responseCode = "400", description = "Erro de validação no registro.", content = {
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = AuthDocs.Register.STATUS_400_RESPONSE)
            )
        }),
    })
    ResponseEntity<RegisterResponse> register(
        @Parameter(
                description = "Dados para registrar um novo usuário.", 
                required = true,
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                schema = @Schema(implementation = RegisterRequest.class))
        ) @Valid @RequestBody RegisterRequest request
    );
}
