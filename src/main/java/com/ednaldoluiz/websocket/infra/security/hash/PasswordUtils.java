package com.ednaldoluiz.websocket.infra.security.hash;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

    private PasswordUtils() {
        throw new UnsupportedOperationException("Utility class");
    }

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String hashPassword(char[] password) {
        String passwordString = new String(password);
        return encoder.encode(passwordString);
    }

    public static boolean verifyPassword(char[] rawPassword, String hashedPassword) {
        String passwordString = new String(rawPassword);
        return encoder.matches(passwordString, hashedPassword);
    }

    public static boolean verifyPassword(String rawPassword, String hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }
}
