package com.ednaldoluiz.websocket.app.v1.auth.usecase;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.OAuth2UserInfoRequest;
import com.ednaldoluiz.websocket.app.v1.auth.dto.response.OAuth2LoginResponse;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.strategy.OAuth2ProviderStrategy;
import com.ednaldoluiz.websocket.app.v1.auth.usecase.strategy.OAuth2ProviderStrategyFactory;
import com.ednaldoluiz.websocket.domain.model.user.AuthProvider;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.UserRepository;
import com.ednaldoluiz.websocket.infra.security.service.JwtService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginUseCase {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final OAuth2ProviderStrategyFactory strategyFactory;

    public OAuth2LoginResponse execute(Map<String, Object> userAttributes, AuthProvider provider) {
        log.info("Processando login via OAuth2 ({}): {}", provider, userAttributes);

        OAuth2ProviderStrategy strategy = strategyFactory.getStrategy(provider);

        strategy.validateAttributes(userAttributes);
        OAuth2UserInfoRequest userInfo = strategy.extractUserInfo(userAttributes);

        Optional<User> userOpt = userRepository.findByEmail(userInfo.email());
        User user = userOpt.orElseGet(() -> {
            log.info("Usuário não encontrado. Criando via OAuth2...");
            User newUser = new User(userInfo.email(), userInfo.name(), userInfo.avatar(), userInfo.bio(), provider);
            userRepository.save(newUser);
            return newUser;
        });

        String token = jwtService.generateToken(user);
        return new OAuth2LoginResponse("Login via " + provider + " com sucesso!", token, userAttributes);
    }
}