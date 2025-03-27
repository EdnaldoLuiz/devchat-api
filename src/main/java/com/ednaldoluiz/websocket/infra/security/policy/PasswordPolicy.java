package com.ednaldoluiz.websocket.infra.security.policy;

import com.ednaldoluiz.websocket.app.v1.auth.dto.response.GeneratedPasswordResponse;
import com.ednaldoluiz.websocket.infra.web.handler.error.FieldErrorResponse;
import com.ednaldoluiz.websocket.infra.web.handler.exception.PasswordValidationException;
import org.passay.*;
import org.passay.dictionary.ArrayWordList;
import org.passay.dictionary.WordListDictionary;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 * Classe responsável por definir a política de senhas e validar senhas
 * de acordo com as regras estabelecidas. Utiliza a biblioteca {@link Passay}
 * para aplicar regras de complexidade e evitar senhas fracas.
 */
@Component
public class PasswordPolicy {

    private static final int MIN_CHAR_OCCURRENCE = 1;
    private static final int MIN_REPEAT_CHARACTERS = 3;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 20;
    private static final int MAX_GENERATED_PASSWORD_LENGTH = 20;

    private static final CharacterRule SPECIAL_CHAR_RULE = new CharacterRule(new CustomSpecialCharacterData(),
            MIN_CHAR_OCCURRENCE);
    private static final CharacterRule LOWERCASE = new CharacterRule(EnglishCharacterData.LowerCase,
            MIN_CHAR_OCCURRENCE);
    private static final CharacterRule UPPERCASE = new CharacterRule(EnglishCharacterData.UpperCase,
            MIN_CHAR_OCCURRENCE);
    private static final CharacterRule NUMBER = new CharacterRule(EnglishCharacterData.Digit, MIN_CHAR_OCCURRENCE);
    private static final LengthRule LENGTH_RULE = new LengthRule(MIN_PASSWORD_LENGTH, MAX_PASSWORD_LENGTH);

    private static final String[] BLACKLISTED_PASSWORDS = {
            "password", "123456", "admin", "qwerty", "abc123", "root"
    };

    private static final String MESSAGE_PROPERTIES = "messages";

    private final PasswordValidator validator;
    private final ResourceBundleMessageResolver resolver;

    public GeneratedPasswordResponse generateSecurePassword() {
        String password = generateStrongPassword();
        int strength = calculateStrength(password);
        return GeneratedPasswordResponse.from(password, strength);
    }

    /**
     * Construtor responsável por inicializar a política de senhas com as regras
     * definidas.
     * Utiliza um {@link ResourceBundleMessageResolver} para carregar mensagens
     * personalizadas
     * de erro do arquivo {@code messages.properties}.
     */
    public PasswordPolicy() {
        this.resolver = new ResourceBundleMessageResolver(ResourceBundle.getBundle(MESSAGE_PROPERTIES));
        this.validator = new PasswordValidator(
                resolver,
                Arrays.asList(
                        LOWERCASE,
                        UPPERCASE,
                        NUMBER,
                        SPECIAL_CHAR_RULE,
                        LENGTH_RULE,
                        new WhitespaceRule(),
                        new DictionaryRule(
                                new WordListDictionary(
                                        new ArrayWordList(
                                                BLACKLISTED_PASSWORDS,
                                                false,
                                                (array, comparator) -> Arrays.sort(array, comparator)))),
                        new RepeatCharacterRegexRule(MIN_REPEAT_CHARACTERS)));
    }

    /**
     * Valida a senha de acordo com as regras definidas.
     *
     * @param password A senha a ser validada.
     * @throws PasswordValidationException Se a senha não for válida.
     */
    public void validatePassword(String password) {
        final RuleResult result = validator.validate(new PasswordData(password));

        if (!result.isValid()) {
            List<FieldErrorResponse> errors = result.getDetails().stream()
                    .map(rule -> new FieldErrorResponse("password", resolver.resolve(rule)))
                    .collect(Collectors.toList());

            throw new PasswordValidationException(errors);
        }
    }

    /**
     * Gera uma senha forte automaticamente seguindo as regras mínimas de
     * complexidade.
     * A senha gerada conterá uma combinação de letras maiúsculas, minúsculas,
     * números
     * e caracteres especiais, com um comprimento definido em
     * {@code MAX_GENERATED_PASSWORD_LENGTH}.
     *
     * @return Uma senha gerada aleatoriamente que atende aos requisitos de
     *         segurança.
     */
    public String generateStrongPassword() {
        PasswordGenerator generator = new PasswordGenerator();
        return generator.generatePassword(
                MAX_GENERATED_PASSWORD_LENGTH,
                Arrays.asList(LOWERCASE, UPPERCASE, NUMBER, SPECIAL_CHAR_RULE));
    }

    /**
     * Calcula a força da senha com base no comprimento e diversidade de
     * caracteres.
     *
     * @param password A senha a ser avaliada.
     * @return A força da senha, de 0 a 100.
     */
    private int calculateStrength(String password) {
        int lengthScore = Math.min(20, password.length()) * 5;
        int diversityScore = (password.chars().distinct().count() > 6) ? 40 : 20;
        return Math.min(100, lengthScore + diversityScore);
    }
}