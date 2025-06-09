package com.ednaldoluiz.websocket.infra.persistence.jdbcrepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ednaldoluiz.websocket.infra.persistence.jdbcrepository.mapper.ChatSummaryProjectionMapper;
import com.ednaldoluiz.websocket.infra.persistence.jdbcrepository.projection.ChatSummaryProjection;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ChatSummaryJdbcRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public List<ChatSummaryProjection> findAllByUserId(Long userId) {

        String sql = """
                    SELECT
                        chat_id,
                        participant_id,
                        room_id,
                        last_message_id,
                        last_message_sender_id,
                        participant_name,
                        participant_avatar,
                        last_message_at,
                        unread_count
                    FROM chat_summaries
                    WHERE user_id = :userId
                    ORDER BY last_message_at DESC
                """;

        var params = Map.of("userId", userId);
        return jdbcTemplate.query(sql, params, new ChatSummaryProjectionMapper());
    }

    public Optional<ChatSummaryProjection> findByUserIdAndChatId(Long userId, Long chatId) {
        String sql = """
                    SELECT chat_id, participant_id, room_id, last_message_id, last_message_sender_id,
                           participant_name, participant_avatar, last_message_at, unread_count
                      FROM chat_summaries
                     WHERE user_id = :userId
                       AND chat_id = :chatId
                """;

        var params = Map.of("userId", userId, "chatId", chatId);
        return jdbcTemplate.query(sql, params, new ChatSummaryProjectionMapper())
                .stream().findFirst();
    }
}
