package com.ednaldoluiz.websocket.infra.security.policy;

import org.passay.CharacterData;

/**
 * Define um conjunto restrito de caracteres especiais permitidos para senhas.
 */
public class CustomSpecialCharacterData implements CharacterData {

    private static final String ERROR_CODE = "INSUFFICIENT_SPECIAL";
    private static final String ALLOWED_CHARACTERS = "!@#$%^&*-_+=<>?";

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }

    @Override
    public String getCharacters() {
        return ALLOWED_CHARACTERS;
    }
}