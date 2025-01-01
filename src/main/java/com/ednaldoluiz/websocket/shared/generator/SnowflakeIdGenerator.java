package com.ednaldoluiz.websocket.shared.generator;

/**
 * Implementação de um Gerador de IDs Snowflake que gera identificadores únicos de 64 bits.
 * Baseado no algoritmo Snowflake do Twitter, esta implementação suporta sistemas distribuídos
 * ao incorporar um timestamp, ID do datacenter, ID da máquina e um número de sequência no ID.
 * 
 * A estrutura do ID gerado é:
 * - 41 bits para o timestamp em milissegundos desde o epoch.
 * - 5 bits para o ID do datacenter.
 * - 5 bits para o ID da máquina.
 * - 12 bits para o número de sequência.
 *
 * Isso garante IDs únicos, ordenados cronologicamente e escaláveis em sistemas distribuídos.
 */
public class SnowflakeIdGenerator {

    /** Epoch que marca o início da geração de IDs (Segunda-feira, 1 de Janeiro de 2024, 00:00:00). */
    private static final long START_TIME_EPOCH = 1704067200000L;

    /** Bits alocados para cada parte. */
    private static final int DATACENTER_ID_BITS = 5;
    private static final int MACHINE_ID_BITS = 5;
    private static final int SEQUENCE_BITS = 12;

    /** Valor máximo para cada parte. */
    private static final long MAX_DATACENTER_ID = ~(-1L << DATACENTER_ID_BITS);
    private static final long MAX_MACHINE_ID = ~(-1L << MACHINE_ID_BITS);
    private static final long MAX_SEQUENCE = ~(-1L << SEQUENCE_BITS);

    /** Deslocamento de bits para cada parte. */
    private static final long MACHINE_ID_SHIFT = SEQUENCE_BITS;
    private static final long DATACENTER_ID_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS;
    private static final long TIMESTAMP_LEFT_SHIFT = SEQUENCE_BITS + MACHINE_ID_BITS + DATACENTER_ID_BITS;

    private static final long INITIAL_TIMESTAMP = -1L;
    private static final long INITIAL_SEQUENCE = 0L;

    private final long datacenterId;
    private final long machineId;

    private long sequence = INITIAL_SEQUENCE;
    private long lastTimestamp = INITIAL_TIMESTAMP;

    /**
     * Constrói um Gerador de IDs Snowflake com os IDs de datacenter e máquina fornecidos.
     *
     * @param datacenterId O ID do datacenter (0 a 31).
     * @param machineId O ID da máquina (0 a 31).
     * @throws IllegalArgumentException se o datacenterId ou machineId estiver fora do intervalo.
     */
    public SnowflakeIdGenerator(final long datacenterId, final long machineId) {
        validateIdRange(datacenterId, MAX_DATACENTER_ID, "Datacenter ID");
        validateIdRange(machineId, MAX_MACHINE_ID, "Machine ID");

        this.datacenterId = datacenterId;
        this.machineId = machineId;
    }

    /**
     * Valida se um ID está dentro do intervalo permitido.
     *
     * @param id O valor do ID a ser validado.
     * @param maxId O valor máximo permitido para o ID.
     * @param idName Nome do ID para mensagens de erro.
     */
    private void validateIdRange(final long id, final long maxId, final String idName) {
        if (id < 0 || id > maxId) {
            throw new IllegalArgumentException(String.format("%s deve estar entre 0 e %d", idName, maxId));
        }
    }

    /**
     * Gera um ID único de 64 bits usando timestamp, IDs e número de sequência.
     *
     * @return Um ID único de 64 bits.
     */
    public synchronized long generateId() {
        final long currentTimestamp = getCurrentTimestamp();

        if (currentTimestamp < lastTimestamp) {
            throw new IllegalStateException("O relógio retrocedeu. Recusando gerar ID.");
        }

        if (currentTimestamp == lastTimestamp) {
            sequence = (sequence + 1) & MAX_SEQUENCE; // Incrementa e reseta quando atinge o máximo.
            if (sequence == INITIAL_SEQUENCE) {
                // Aguarda o próximo milissegundo se a sequência atingir o limite.
                return generateIdOnNextMillis();
            }
        } else {
            sequence = INITIAL_SEQUENCE;
        }

        lastTimestamp = currentTimestamp;
        return assembleId(currentTimestamp);
    }

    /**
     * Monta o ID final com base no timestamp, datacenter ID, machine ID e sequência.
     *
     * @param timestamp O timestamp atual.
     * @return O ID gerado.
     */
    private long assembleId(final long timestamp) {
        return ((timestamp - START_TIME_EPOCH) << TIMESTAMP_LEFT_SHIFT) |
               (datacenterId << DATACENTER_ID_SHIFT) |
               (machineId << MACHINE_ID_SHIFT) |
               sequence;
    }

    /**
     * Gera um novo ID aguardando o próximo milissegundo.
     *
     * @return O ID gerado.
     */
    private long generateIdOnNextMillis() {
        lastTimestamp = waitForNextMillis(lastTimestamp);
        return assembleId(lastTimestamp);
    }

    /**
     * Aguarda até o próximo milissegundo para garantir um timestamp único.
     *
     * @param lastTimestamp O timestamp do último ID gerado.
     * @return O timestamp atual em milissegundos.
     */
    private long waitForNextMillis(final long lastTimestamp) {
        long timestamp;
        do {
            timestamp = getCurrentTimestamp();
        } while (timestamp <= lastTimestamp);
        return timestamp;
    }

    /**
     * Obtém o timestamp atual do sistema.
     *
     * @return O timestamp atual em milissegundos.
     */
    private long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }
}
