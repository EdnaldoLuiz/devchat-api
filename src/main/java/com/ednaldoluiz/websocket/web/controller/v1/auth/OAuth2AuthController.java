package com.ednaldoluiz.websocket.web.controller.v1.auth;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ednaldoluiz.websocket.app.v1.auth.dto.response.OAuth2LoginResponse;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.OAuth2LoginUseCase;
import com.ednaldoluiz.websocket.domain.model.user.AuthProvider;
import com.ednaldoluiz.websocket.web.route.Paths;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;

@Hidden
@Controller
@RequestMapping(Paths.V1.Auth.AUTH + "/oauth2")
@RequiredArgsConstructor
public class OAuth2AuthController {

    private final OAuth2LoginUseCase oAuth2LoginUseCase;

    @GetMapping("/login/{provider}")
    public String redirectToProvider(@PathVariable String provider) {
        return String.format("redirect:/oauth2/authorization/%s", provider.toLowerCase());
    }

    @GetMapping("/success")
    public ResponseEntity<OAuth2LoginResponse> loginSuccess() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthenticationToken oauth2Token = (OAuth2AuthenticationToken) auth;
        String registrationId = oauth2Token.getAuthorizedClientRegistrationId().toUpperCase();
        OAuth2LoginResponse response = oAuth2LoginUseCase.execute(
            oauth2Token.getPrincipal().getAttributes(),
            AuthProvider.valueOf(registrationId)
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/failure")
    public ResponseEntity<String> loginFailure() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Falha no login via GitHub");
    }
}