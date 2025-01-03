package com.ednaldoluiz.websocket.shared.utils;

import java.util.Arrays;

/**
 * Classe utilitária para operações genéricas com enums.
 */
public final class EnumUtils {

    private EnumUtils() {}

    /**
     * Converte uma string em um valor de enum, retornando um valor padrão caso não seja possível encontrar o enum correspondente.
     *
     * @param <E>         Tipo do enum.
     * @param enumClass   Classe do enum.
     * @param value       String representando o valor do enum.
     * @param defaultEnum Valor padrão a ser retornado caso a conversão falhe.
     * @return Valor do enum correspondente ou o valor padrão.
     */
    public static <E extends Enum<E>> E fromString(Class<E> enumClass, String value, E defaultEnum) {
        if (value == null) {
            return defaultEnum;
        }

        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> e.toString().equalsIgnoreCase(value))
                .findFirst()
                .orElse(defaultEnum);
    }
}
