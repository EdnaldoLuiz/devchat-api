package com.ednaldoluiz.websocket.infra.web.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response.RegisterResponse;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.port.RegisterUserUseCasePort;
import com.ednaldoluiz.websocket.infra.web.docs.AuthControllerDocs;
import com.ednaldoluiz.websocket.infra.web.route.Paths;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(path = Paths.V1.Auth.AUTH, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Controller de Autenticação", description = "Controller usado para operações de autenticação e senhas.")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCasePort registerPort;

    @PostMapping(Paths.Auth.REGISTER)
    @Operation(
        summary = AuthControllerDocs.REGISTER_SUMMARY, 
        description = AuthControllerDocs.REGISTER_DESCRIPTION
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {
                    @Content(schema = @Schema(implementation = RegisterRequest.class), mediaType = MediaType.APPLICATION_JSON_VALUE)
            }),
            @ApiResponse(responseCode = "400", content = {
                    @Content(schema = @Schema())
            })
    })
    public ResponseEntity<RegisterResponse> register(
            @Valid @RequestBody RegisterRequest request
        ) {
        RegisterResponse response = registerPort.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
