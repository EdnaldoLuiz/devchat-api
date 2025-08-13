package com.ednaldoluiz.websocket.infra.persistence.jdbcrepository.mapper;

import com.ednaldoluiz.websocket.infra.persistence.jdbcrepository.projection.SignalHandshakeBundleProjection;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class SignalHandshakeBundleProjectionMapper implements RowMapper<SignalHandshakeBundleProjection> {

    @Override
    public SignalHandshakeBundleProjection mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new SignalHandshakeBundleProjection(
            rs.getInt("registration_id"),
            rs.getBytes("identity_key"),
            rs.getInt("signed_pre_key_id"),
            rs.getBytes("signed_pre_key"),
            rs.getBytes("signed_pre_sig"),
            rs.getTimestamp("created_at").toInstant()
        );
    }
}
