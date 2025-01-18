package com.ednaldoluiz.websocket.infra.security.policy;

import org.passay.*;
import org.passay.dictionary.ArrayWordList;
import org.passay.dictionary.WordListDictionary;

import java.util.Arrays;
import java.util.List;

/**
 * Classe responsável por definir a política de senhas utilizando a biblioteca Passay.
 */
public class PasswordPolicy {

    private static final int MIN_CHAR_OCCURRENCE = 1;
    private static final int MIN_REPEAT_CHARACTERS = 3;
    private static final int MIN_PASSWORD_LENGTH = 6;

    private static final int MAX_PASSWORD_LENGTH = 16;
    private static final int MAX_GENERATED_PASSWORD_LENGTH = 12;

    private static final CharacterRule SPECIAL_CHAR_RULE = new CharacterRule(EnglishCharacterData.Special, MIN_CHAR_OCCURRENCE);
    private static final CharacterRule LOWERCASE = new CharacterRule(EnglishCharacterData.LowerCase, MIN_CHAR_OCCURRENCE);
    private static final CharacterRule UPPERCASE = new CharacterRule(EnglishCharacterData.UpperCase, MIN_CHAR_OCCURRENCE);
    private static final CharacterRule NUMBER = new CharacterRule(EnglishCharacterData.Digit, MIN_CHAR_OCCURRENCE);
    private static final LengthRule LENGTH_RULE =new LengthRule(MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH);

    /**
     * Exemplo de senhas que devem ser rejeitadas completamente (blacklist).
     */
    private static final String[] BLACKLISTED_PASSWORDS = {
        "password", "123456", "admin", "qwerty", "abc123", "root"
    };

    private final PasswordValidator validator;

    /**
     * Inicializa a política de senhas com o conjunto de regras definido.
     */
    public PasswordPolicy() {
        this.validator = new PasswordValidator(
            Arrays.asList(
                LOWERCASE,
                UPPERCASE,
                NUMBER,
                SPECIAL_CHAR_RULE,
                LENGTH_RULE,
                new WhitespaceRule(),
                new DictionaryRule(
                    new WordListDictionary(
                        new ArrayWordList(BLACKLISTED_PASSWORDS, false)
                    )
                ),
                new RepeatCharacterRegexRule(MIN_REPEAT_CHARACTERS)
            )
        );
    }

    /**
     * Valida a senha de acordo com as regras definidas.
     *
     * @param password A senha a ser validada.
     * @throws IllegalArgumentException Se a senha não for válida.
     */
    public void validatePassword(String password) {
        final RuleResult result = validator.validate(new PasswordData(password));
        if (!result.isValid()) {
            List<String> errorMessages = validator.getMessages(result);
            throw new IllegalArgumentException(
                "Senha inválida: " + String.join("; ", errorMessages)
            );
        }
    }

    /**
     * Gera uma senha forte com base em algumas regras básicas.
     * <p>
     * É possível variar as regras de geração para se adaptar ao que foi definido
     * no conjunto de validação.
     *
     * @return Uma senha gerada que atenda aos requisitos mínimos.
     */
    public String generateStrongPassword() {
        PasswordGenerator generator = new PasswordGenerator();
        return generator.generatePassword(
            MAX_GENERATED_PASSWORD_LENGTH, 
            Arrays.asList(LOWERCASE, UPPERCASE, NUMBER, SPECIAL_CHAR_RULE)
        );
    }
}