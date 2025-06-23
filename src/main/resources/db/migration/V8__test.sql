/* ---------- CHAVES PÚBLICAS & PRE‑KEYS ---------- */
CREATE TABLE user_signal_keys (
                                  user_id           BIGINT      PRIMARY KEY,
                                  registration_id   INT         NOT NULL,
                                  identity_key      BLOB        NOT NULL,
                                  signed_pre_key_id INT         NOT NULL,
                                  signed_pre_key    BLOB        NOT NULL,
                                  signed_pre_sig    BLOB        NOT NULL,
                                  created_at        TIMESTAMP   DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE signal_one_time_pre_keys (
                                          user_id    BIGINT  NOT NULL,
                                          key_id     INT     NOT NULL,
                                          pre_key    BLOB    NOT NULL,
                                          consumed   BOOLEAN DEFAULT FALSE,
                                          PRIMARY KEY (user_id, key_id),
                                          FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
) ENGINE=InnoDB;