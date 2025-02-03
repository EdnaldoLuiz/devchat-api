package com.ednaldoluiz.websocket.infra.annotation.phone;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    // Pattern para verificar se todos os dígitos são iguais (ex.: "11111111111")
    private static final Pattern REPEATED_PATTERN = Pattern.compile("^(.)\\1*$"); // 

    private static final Set<Integer> VALID_DDD = new HashSet<>(Set.of(
        11, 12, 13, 14, 15, 16, 17, 18, 19, 21, 22, 24, 27, 28, 31, 32, 33, 34, 35, 
        37, 38, 41, 42, 43, 44, 45, 46, 47, 48, 49, 51, 53, 54, 55, 61, 62, 63, 64, 
        65, 66, 67, 68, 69, 71, 73, 74, 75, 77, 79, 81, 82, 83, 84, 85, 86, 87, 88, 
        89, 91, 92, 93, 94, 95, 96, 97, 98, 99
    ));

    private static final Set<Integer> VALID_PREFIXES = new HashSet<>(Set.of(2, 3, 4, 5, 7));

    @Override
    public boolean isValid(String telefone, ConstraintValidatorContext context) {
        if (telefone == null || telefone.isBlank()) {
            return false;
        }

        String digits = extractDigits(telefone);
        if (!validateLength(digits)) return false;
        if (!validateElevenDigitsRule(digits)) return false;
        if (!validateRepeatedDigits(digits)) return false;
        if (!validateDDDAndPrefix(digits)) return false;
        return true;
    }

    private String extractDigits(String telefone) {
        return telefone.replaceAll("\\D", "");
    }

    private boolean validateLength(String digits) {
        int length = digits.length();
        return length == 10 || length == 11;
    }

    private boolean validateElevenDigitsRule(String digits) {
        if (digits.length() == 11) {
            try {
                return Integer.parseInt(digits.substring(2, 3)) == 9;
            } catch (NumberFormatException e) {
                return false;
            }
        }
        return true;
    }

    private boolean validateRepeatedDigits(String digits) {
        return !REPEATED_PATTERN.matcher(digits).matches();
    }

    private boolean validateDDDAndPrefix(String digits) {
        try {
            int ddd = Integer.parseInt(digits.substring(0, 2));
            if (!VALID_DDD.contains(ddd)) {
                return false;
            }
            if (digits.length() == 10) {
                int prefix = Integer.parseInt(digits.substring(2, 3));
                return VALID_PREFIXES.contains(prefix);
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}