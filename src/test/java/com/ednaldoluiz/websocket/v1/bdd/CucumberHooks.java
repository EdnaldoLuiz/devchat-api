package com.ednaldoluiz.websocket.v1.bdd;

import org.springframework.beans.factory.annotation.Autowired;

import com.ednaldoluiz.websocket.infra.persistence.PasswordResetTokenRepository;
import com.ednaldoluiz.websocket.infra.persistence.UserRepository;

import io.cucumber.java.Before;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CucumberHooks {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Before("@auth")
    public void limparBancoDeDadosAntesDoRegistro() {
        log.info("Limpando banco de dados...");
        userRepository.deleteAll();
        tokenRepository.deleteAll();
        log.info("Banco de dados limpo na classe: {}.", this.getClass().getSimpleName());
    }
}
