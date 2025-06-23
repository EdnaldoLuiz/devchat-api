package com.ednaldoluiz.websocket.web.controller.v1.signal;

import com.ednaldoluiz.websocket.web.websocket.store.AuthUser;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.ednaldoluiz.websocket.app.v1.signal.dto.request.KeyBundleRequestDTO;
import com.ednaldoluiz.websocket.app.v1.signal.dto.request.RotateSignedPreKeyRequestDTO;
import com.ednaldoluiz.websocket.app.v1.signal.dto.response.PreKeyBundleResponseDTO;
import com.ednaldoluiz.websocket.app.v1.signal.dto.response.SignalKeyStatusResponseDTO;
import com.ednaldoluiz.websocket.web.controller.common.GenericApiResponse;
import com.ednaldoluiz.websocket.web.handler.error.ErrorResponse;
import com.ednaldoluiz.websocket.web.route.Paths;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RequestMapping(path = Paths.V1.Signal.SIGNAL, produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Signal Keys", description = "Gerenciamento seguro do bundle de chaves E2EE para WebSocket/Signal.")
public interface SignalKeyApi {

    /**
     * Endpoint para upload do bundle de chaves do Signal.
     * <p>
     * O bundle deve conter a signedPreKey e pelo menos uma oneTimePreKey.
     *
     * @param dto  o bundle de chaves a ser salvo
     * @param user o usuário autenticado
     * @return resposta genérica indicando sucesso ou falha
     */

    @PostMapping(Paths.Signal.SAVE_KEY_BUNDLE)
    @Operation(summary = SignalKeyDocs.UploadBundle.SUMMARY, description = SignalKeyDocs.UploadBundle.DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Key bundle salvo com sucesso.", content = @Content(schema = @Schema(implementation = GenericApiResponse.class), examples = @ExampleObject(value = SignalKeyDocs.UploadBundle.STATUS_200_RESPONSE))),
            @ApiResponse(responseCode = "400", description = "Request inválido ou mal formatado.", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = SignalKeyDocs.UploadBundle.STATUS_400_RESPONSE))),
    })
    ResponseEntity<GenericApiResponse> uploadKeyBundle(
            @Parameter(description = "Bundle completo de chaves do Signal.", required = true) @Valid @RequestBody KeyBundleRequestDTO dto,
            @AuthenticationPrincipal AuthUser user);

    /**
     * Endpoint para buscar o bundle de chaves pre-key de um usuário.
     * <p>
     * Retorna o bundle de pre-keys do usuário especificado.
     * 
     * @param targetId o ID do usuário alvo
     * @param user     o usuário autenticado
     * @return o bundle de pre-keys do usuário alvo
     */

    @GetMapping(Paths.Signal.FETCH_KEY_BUNDLE)
    @Operation(summary = SignalKeyDocs.FetchPreKeyBundle.SUMMARY, description = SignalKeyDocs.FetchPreKeyBundle.DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = PreKeyBundleResponseDTO.class), examples = @ExampleObject(value = SignalKeyDocs.FetchPreKeyBundle.STATUS_200_RESPONSE))),
            @ApiResponse(responseCode = "410", description = "Sem pre-keys", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = SignalKeyDocs.FetchPreKeyBundle.STATUS_410_RESPONSE))),
            @ApiResponse(responseCode = "404", description = "Bundle não encontrado", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = SignalKeyDocs.FetchPreKeyBundle.STATUS_404_RESPONSE)))
    })
    ResponseEntity<PreKeyBundleResponseDTO> fetchPreKeyBundle(
            @PathVariable Long targetId,
            @AuthenticationPrincipal AuthUser user);

    /**
     * Endpoint para rotacionar a signedPreKey do dispositivo.
     * <p>
     * Gera uma nova signedPreKey e atualiza o bundle de chaves do usuário.
     * 
     * @param dto  a solicitação de rotação da signedPreKey
     * @param user
     *             o usuário autenticado
     * @return resposta genérica indicando sucesso ou falha
     */

    @PostMapping(Paths.Signal.ROTATE_SPK)
    @Operation(summary = SignalKeyDocs.RotateSPK.SUMMARY, description = SignalKeyDocs.RotateSPK.DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "SignedPreKey rotacionada com sucesso.", content = @Content(schema = @Schema(implementation = GenericApiResponse.class), examples = @ExampleObject(value = SignalKeyDocs.RotateSPK.STATUS_200_RESPONSE))),
            @ApiResponse(responseCode = "400", description = "Request inválido", content = @Content(schema = @Schema(implementation = ErrorResponse.class), examples = @ExampleObject(value = SignalKeyDocs.RotateSPK.STATUS_400_RESPONSE)))
    })
    ResponseEntity<GenericApiResponse> rotateSignedPreKey(
            @Valid @RequestBody RotateSignedPreKeyRequestDTO dto,
            @AuthenticationPrincipal AuthUser user);

    /**
     * Endpoint para consultar o status das chaves do Signal.
     * <p>
     * Retorna o status do estoque de pre-keys e a validade da signedPreKey.
     * 
     * @param user o usuário autenticado
     * @return o status das chaves do Signal
     */
    @GetMapping(Paths.Signal.STATUS)
    @Operation(summary = SignalKeyDocs.Status.SUMMARY, description = SignalKeyDocs.Status.DESCRIPTION)
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Status das chaves retornado com sucesso.", content = @Content(schema = @Schema(implementation = SignalKeyStatusResponseDTO.class), examples = @ExampleObject(value = SignalKeyDocs.Status.STATUS_200_RESPONSE)))
    })
    ResponseEntity<SignalKeyStatusResponseDTO> getKeyStatus(
            @AuthenticationPrincipal AuthUser user);

}
