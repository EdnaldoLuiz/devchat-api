package com.ednaldoluiz.websocket.v1.shared.base;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ednaldoluiz.websocket.app.v1.auth.dto.request.RegisterRequest;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.repository.UserRepository;

public abstract class AbstractAuthTest extends AbstractApiTest {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    protected User insertUserIntoDatabase(RegisterRequest request) {
        log.info("Inserindo usuário no banco: {} pela classe {}", request.email(), this.getClass().getSimpleName());
        User userEntity = new User(
                request.email(),
                passwordEncoder.encode(request.password()),
                request.name());
        userRepository.persist(userEntity);
        log.info("Usuário salvo no banco: {}", userRepository.findByEmail(request.email()));
        return userEntity;
    }
}