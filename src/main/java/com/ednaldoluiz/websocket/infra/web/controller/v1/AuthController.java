package com.ednaldoluiz.websocket.infra.web.controller.v1;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request.LoginRequest;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response.GeneratedPasswordResponse;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response.LoginResponse;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response.LogoutResponse;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.dto.response.RegisterResponse;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.port.GeneratePasswordUseCasePort;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.port.LoginUseCasePort;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.port.RegisterUserUseCasePort;
import com.ednaldoluiz.websocket.infra.web.docs.AuthDocs;
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

@RestController
@RequestMapping(
    path = Paths.V1.Auth.AUTH, 
    produces = MediaType.APPLICATION_JSON_VALUE
)
@Tag(name = "Autenticação", description = "Endpoints responsáveis pelo registro e autenticação de usuários.")
@RequiredArgsConstructor
public class AuthController {

    private final RegisterUserUseCasePort registerPort;
    private final LoginUseCasePort loginPort;
    private final GeneratePasswordUseCasePort generatePasswordUseCase;

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
    public ResponseEntity<RegisterResponse> register(
            @Parameter(
                description = "Dados para registrar um novo usuário.", 
                required = true,
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                schema = @Schema(implementation = RegisterRequest.class))
            ) @Valid @RequestBody RegisterRequest request
        ) {
        RegisterResponse response = registerPort.register(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Endpoint para autenticar um usuário.
     *
     * @param request Objeto contendo os dados necessários para a autenticação do usuário.
     * @return Objeto contendo ID, email, token e refresh token do usuário autenticado.
     */
    @PostMapping(Paths.Auth.LOGIN)
    @Operation(
        summary = AuthDocs.Login.SUMMARY, 
        description = AuthDocs.Login.DESCRIPTION
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Usuário autenticado com sucesso.", content = {
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE, 
                schema = @Schema(implementation = RegisterResponse.class),
                examples = @ExampleObject(value = AuthDocs.Login.STATUS_200_RESPONSE)
            )
        }),
        @ApiResponse(responseCode = "401", description = "Erro de validação no login.", content = {
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(value = AuthDocs.Login.STATUS_401_RESPONSE)
            )
        }),
    })
    public ResponseEntity<LoginResponse> login(
            @Parameter(
                description = "Dados para autenticar um usuário.", 
                required = true,
                content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, 
                schema = @Schema(implementation = LoginRequest.class))
            ) @Valid @RequestBody LoginRequest request
        ) {
        LoginResponse response = loginPort.login(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint para gerar uma senha segura automaticamente.
     *
     * @return Senha gerada conforme as regras de complexidade definidas.
     */
    @GetMapping(Paths.Auth.GENERATE_PASSWORD)
    @Operation(
        summary = AuthDocs.GeneratePassword.SUMMARY, 
        description = AuthDocs.GeneratePassword.DESCRIPTION
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Senha gerada com sucesso.", content = {
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                examples = @ExampleObject(value = AuthDocs.GeneratePassword.STATUS_200_RESPONSE)
            )
        })
    })
    public ResponseEntity<GeneratedPasswordResponse> generateStrongPassword() {
        GeneratedPasswordResponse password = generatePasswordUseCase.generateStrongPassword();
        return ResponseEntity.ok(password);
    }

    /**
     * Endpoint para realizar o logout de um usuário.
     *
     * @return Mensagem de sucesso no logout.
     */
    @PostMapping(Paths.Auth.LOGOUT)
    @Operation(
        summary = "Realiza o logout do usuário",
        description = "Invalida o token do usuário atual e remove a sessão ativa."
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Logout realizado com sucesso.", content = {
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = LogoutResponse.class)
            )
        }),
        @ApiResponse(responseCode = "401", description = "Usuário não autenticado.", content = {
            @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ErrorResponse.class)
            )
        })
    })
    public ResponseEntity<LogoutResponse> logout() {
        return ResponseEntity.ok(LogoutResponse.success());
    }
}