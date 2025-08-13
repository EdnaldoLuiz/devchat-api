package com.ednaldoluiz.websocket.domain.model.message;

/**
 * Tipos de envelope criptográfico suportados no back-end.
 *
 * <p>Mapeamento conceitual (libsignal):
 * <ul>
 *   <li>{@link #WHISPER} → {@code WhisperMessage}: usado quando a sessão já está estabelecida.</li>
 *   <li>{@link #PREKEY_WHISPER} → {@code PreKeyWhisperMessage}: usado para o primeiro envio ao peer,
 *       consumindo uma one-time pre-key e (geralmente) criando a sessão no destinatário.</li>
 *   <li>{@link #SENDERKEY} → {@code SenderKeyMessage}: usado em conversas em grupo com chaves de remetente
 *       (mais eficiente que enviar N cópias unicast).</li>
 * </ul>
 *
 * <p>Persistimos como SMALLINT para economia de espaço e facilidade de indexação.
 * O {@code code} é o valor exato salvo na coluna {@code messages.cipher_type}.
 */
public enum CipherType {
    /** Sessão ponto-a-ponto já estabelecida (unicast, rápido, sem consumo de pre-key). */
    WHISPER(1),

    /** Primeiro contato / (re)inicialização de sessão usando uma one-time pre-key do destinatário. */
    PREKEY_WHISPER(3),

    /** Mensagem de grupo baseada em Sender Keys (eficiente para múltiplos destinatários). */
    SENDERKEY(2);

    private final short code;

    CipherType(int code) { this.code = (short) code; }

    /** Valor inteiro persistido no banco (SMALLINT). */
    public short getCode() { return code; }

    /** Converte o código persistido para o enum. */
    public static CipherType from(short code) {
        for (var v : values()) if (v.code == code) return v;
        throw new IllegalArgumentException("Unknown cipherType code: " + code);
    }

    /** Retorna uma descrição humana (útil para logs/diagnóstico). */
    public String description() {
        return switch (this) {
            case WHISPER -> "Sessão estabelecida (WhisperMessage)";
            case PREKEY_WHISPER -> "Primeiro contato (PreKeyWhisperMessage)";
            case SENDERKEY -> "Grupo com Sender Keys (SenderKeyMessage)";
        };
    }

    /** Conveniência: é mensagem inicial (consome pre-key)? */
    public boolean isPreKey() { return this == PREKEY_WHISPER; }

    /** Conveniência: é mensagem de grupo (sender keys)? */
    public boolean isGroup() { return this == SENDERKEY; }

    /** Tipo padrão recomendado para P2P após sessão criada. */
    public static CipherType defaultP2P() { return WHISPER; }
}
