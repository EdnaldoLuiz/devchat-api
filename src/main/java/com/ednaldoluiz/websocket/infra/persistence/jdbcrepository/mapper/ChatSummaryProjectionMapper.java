package com.ednaldoluiz.websocket.infra.persistence.jdbcrepository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.springframework.jdbc.core.RowMapper;

import com.ednaldoluiz.websocket.infra.persistence.jdbcrepository.projection.ChatSummaryProjection;

public class ChatSummaryProjectionMapper implements RowMapper<ChatSummaryProjection> {

    @Override
    public ChatSummaryProjection mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long participantId = getNullableLong(rs, "participant_id");
        Long roomId = getNullableLong(rs, "room_id");
        Long lastMsgId = getNullableLong(rs, "last_message_id");
        Long lastMsgSenderId = getNullableLong(rs, "last_message_sender_id");

        LocalDateTime lastAt = null;
        Timestamp ts = rs.getTimestamp("last_message_at");
        if (ts != null)
            lastAt = ts.toLocalDateTime();

        return new ChatSummaryProjection(
                rs.getLong("chat_id"),
                participantId,
                roomId,
                lastMsgId,
                lastMsgSenderId,
                rs.getString("participant_name"),
                rs.getString("participant_avatar"),
                lastAt,
                rs.getInt("unread_count"));
    }

    private Long getNullableLong(ResultSet rs, String col) throws SQLException {
        long value = rs.getLong(col);
        return rs.wasNull() ? null : value;
    }
}