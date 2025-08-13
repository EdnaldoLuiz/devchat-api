package com.ednaldoluiz.websocket.infra.persistence.jdbcrepository;

import com.ednaldoluiz.websocket.app.v1.signal.dto.request.KeyBundleRequestDTO;
import com.ednaldoluiz.websocket.domain.model.signal.SignalPreKey;
import com.ednaldoluiz.websocket.infra.persistence.jdbcrepository.mapper.SignalHandshakeBundleProjectionMapper;
import com.ednaldoluiz.websocket.infra.persistence.jdbcrepository.projection.SignalHandshakeBundleProjection;
import com.ednaldoluiz.websocket.infra.persistence.repository.UserSignalKeysRepository;

import com.ednaldoluiz.websocket.infra.persistence.utils.BatchStreamUtils;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.*;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.BiFunction;
import java.util.stream.Stream;

@Repository
@RequiredArgsConstructor
public class UserSignalKeysJdbcRepository implements UserSignalKeysRepository {

    private static final String PARAM_USER_ID = "userId";
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Optional<SignalHandshakeBundleProjection> findKeysByUserIdForHandshake(Long userId) {
        String sql = """
                    SELECT
                        registration_id,
                        identity_key,
                        signed_pre_key_id,
                        signed_pre_key,
                        signed_pre_sig,
                        created_at
                    FROM user_signal_keys
                    WHERE user_id = :userId
                """;

        var result = jdbcTemplate.query(
                sql,
                Map.of("userId", userId),
                new SignalHandshakeBundleProjectionMapper());
        return result.stream().findFirst();
    }

    @Transactional
    public void saveKeyBundle(Long userId, KeyBundleRequestDTO dto) {

        String upsertMain = """
                    INSERT INTO user_signal_keys
                        (user_id, registration_id, identity_key, signed_pre_key_id, signed_pre_key, signed_pre_sig)
                    VALUES
                        (:userId, :registrationId, :identityKey, :signedPreKeyId, :signedPreKey, :signedPreKeySig)
                    ON DUPLICATE KEY UPDATE
                        registration_id = VALUES(registration_id),
                        identity_key = VALUES(identity_key),
                        signed_pre_key_id = VALUES(signed_pre_key_id),
                        signed_pre_key = VALUES(signed_pre_key),
                        signed_pre_sig = VALUES(signed_pre_sig)
                """;

        jdbcTemplate.update(upsertMain, new MapSqlParameterSource()
                .addValue(PARAM_USER_ID, userId)
                .addValue("registrationId", dto.registrationId())
                .addValue("identityKey", dto.identityKey())
                .addValue("signedPreKeyId", dto.signedPreKeyId())
                .addValue("signedPreKey", dto.signedPreKey())
                .addValue("signedPreKeySig", dto.signedPreKeySig()));

        String deletePreKeys = "DELETE FROM signal_one_time_pre_keys WHERE user_id = :userId";
        jdbcTemplate.update(deletePreKeys, Map.of(PARAM_USER_ID, userId));

        String insertPreKey = """
                    INSERT INTO signal_one_time_pre_keys (user_id, key_id, pre_key, consumed)
                    VALUES (:userId, :keyId, :preKey, false)
                """;

        List<MapSqlParameterSource> batchArgs = dto.oneTimePreKeys().stream().map(preKey -> new MapSqlParameterSource()
                .addValue(PARAM_USER_ID, userId)
                .addValue("keyId", preKey.keyId())
                .addValue("preKey", preKey.publicKey())).toList();

        jdbcTemplate.batchUpdate(insertPreKey, batchArgs.toArray(new MapSqlParameterSource[0]));
    }

    @Override
    public Stream<Long> streamAllUserIds(int batchSize) {
        BiFunction<Long, Integer, List<Long>> fetchBatch = (lastId, limit) -> {

            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("batchSize", limit);

            String sql;

            if (lastId == null) {
                sql = """
                            SELECT user_id
                              FROM user_signal_keys
                             ORDER BY user_id
                             LIMIT :batchSize
                        """;
            } else {
                sql = """
                            SELECT user_id
                              FROM user_signal_keys
                             WHERE user_id > :lastId
                             ORDER BY user_id
                             LIMIT :batchSize
                        """;
                params.addValue("lastId", lastId);
            }

            return jdbcTemplate.queryForList(sql, params, Long.class);
        };

        return BatchStreamUtils.streamByBatch(null, batchSize, fetchBatch, id -> id);
    }

    @Override
    public Stream<Long> streamExpiredSignedPreKeyUserIds(int spkTtlDays, int batchSize) {
        BiFunction<Long, Integer, List<Long>> fetchBatch = (lastId, limit) -> {
            String sql;
            MapSqlParameterSource params = new MapSqlParameterSource()
                    .addValue("ttl", spkTtlDays)
                    .addValue("batchSize", limit);

            if (lastId == null) {
                sql = """
                            SELECT user_id
                              FROM user_signal_keys
                             WHERE created_at < (NOW() - INTERVAL :ttl DAY)
                             ORDER BY user_id
                             LIMIT :batchSize
                        """;
            } else {
                sql = """
                            SELECT user_id
                              FROM user_signal_keys
                             WHERE created_at < (NOW() - INTERVAL :ttl DAY)
                               AND user_id > :lastId
                             ORDER BY user_id
                             LIMIT :batchSize
                        """;
                params.addValue("lastId", lastId);
            }

            return jdbcTemplate.queryForList(sql, params, Long.class);
        };

        return BatchStreamUtils.streamByBatch(null, batchSize, fetchBatch, id -> id);
    }

    @Override
    public void deleteByUserId(Long userId) {
        String sql = "DELETE FROM user_signal_keys WHERE user_id = :userId";
        jdbcTemplate.update(sql, Map.of(PARAM_USER_ID, userId));
    }

    @Override
    @Transactional
    public Optional<SignalPreKey> fetchAndConsumePreKey(Long userId) {

        String select = """
                    SELECT key_id, pre_key
                      FROM signal_one_time_pre_keys
                     WHERE user_id = :uid AND consumed = FALSE
                     LIMIT 1
                     FOR UPDATE
                """;

        return jdbcTemplate.query(select, Map.of("uid", userId), rs -> {
            if (!rs.next())
                return Optional.empty();

            int keyId = rs.getInt("key_id");
            byte[] pk = rs.getBytes("pre_key");

            jdbcTemplate.update("""
                        UPDATE signal_one_time_pre_keys
                           SET consumed = TRUE
                         WHERE user_id = :uid AND key_id = :kid
                    """, Map.of("uid", userId, "kid", keyId));

            return Optional.of(new SignalPreKey(userId, keyId, pk, true));
        });
    }

    @Override
    public void updateSignedPreKey(Long userId, int id, byte[] key, byte[] sig) {

        String sql = """
                    UPDATE user_signal_keys
                       SET signed_pre_key_id = :id,
                           signed_pre_key    = :key,
                           signed_pre_sig    = :sig,
                           created_at        = NOW()
                    WHERE user_id = :userId
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_USER_ID, userId)
                .addValue("id", id)
                .addValue("key", key)
                .addValue("sig", sig);

        jdbcTemplate.update(sql, params);
    }

    @Override
    public int countAvailablePreKeys(Long userId) {

        String sql = """
                    SELECT COUNT(*)
                      FROM signal_one_time_pre_keys
                     WHERE user_id = :userId
                       AND consumed = FALSE
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_USER_ID, userId);

        return jdbcTemplate.queryForObject(sql, params, Integer.class);
    }

    @Override
    public Instant getSignedPreKeyCreatedAt(Long userId) {
        String sql = """
                    SELECT created_at
                      FROM user_signal_keys
                     WHERE user_id = :userId
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue(PARAM_USER_ID, userId);

        return jdbcTemplate.queryForObject(sql, params, Instant.class);
    }
}