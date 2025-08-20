-- MENSAGEM LÓGICA (sem ciphertext)
CREATE TABLE messages (
  id            BIGINT PRIMARY KEY AUTO_INCREMENT,
  chat_id       BIGINT       NOT NULL,
  user_id       BIGINT       NOT NULL,
  message_uuid  BINARY(16)   NOT NULL UNIQUE,
  history_algorithm              VARCHAR(32)      NOT NULL,
  history_version                SMALLINT         NOT NULL,
  history_initialization_vector  VARBINARY(12) NOT NULL,
  history_ciphertext             MEDIUMBLOB,
  sent_at       DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  deleted       BOOLEAN      NOT NULL DEFAULT FALSE,
  CONSTRAINT fk_msg_chat  FOREIGN KEY (chat_id) REFERENCES chats(id),
  CONSTRAINT fk_msg_user  FOREIGN KEY (user_id) REFERENCES users(id),
  INDEX ix_msg_chat_ts (chat_id, sent_at, id)
) ENGINE=InnoDB;

-- CÓPIAS POR DESTINATÁRIO (cada usuário recebe seu envelope)
CREATE TABLE message_copies (
  id              BIGINT PRIMARY KEY AUTO_INCREMENT,
  chat_id         BIGINT       NOT NULL,
  message_id      BIGINT       NOT NULL,
  target_user_id  BIGINT       NOT NULL,           -- dono desta cópia
  cipher_type     TINYINT      NOT NULL,           -- 1=Whisper, 3=PreKey (etc.)
  cipher_body     MEDIUMBLOB   NOT NULL,
  sent_at         DATETIME(6)  NOT NULL,           -- copia o messages.sent_at (para index cobrir)
  CONSTRAINT fk_mc_chat    FOREIGN KEY (chat_id)        REFERENCES chats(id),
  CONSTRAINT fk_mc_msg     FOREIGN KEY (message_id)     REFERENCES messages(id) ON DELETE CASCADE,
  CONSTRAINT fk_mc_target  FOREIGN KEY (target_user_id) REFERENCES users(id),
  CONSTRAINT uq_mc UNIQUE (message_id, target_user_id), -- evita duplicatas

  -- índices práticos p/ histórico
  INDEX ix_mc_target_chat_ts (target_user_id, chat_id, sent_at, id),
  INDEX ix_mc_chat_target_ts (chat_id, target_user_id, sent_at, id)
) ENGINE=InnoDB;

-- ANEXOS: só metadata pública (conteúdo em storage próprio e cifrado client-side)
CREATE TABLE message_attachments (
  id               BIGINT PRIMARY KEY AUTO_INCREMENT,
  message_id       BIGINT       NOT NULL,
  attachment_type  ENUM('IMAGE','VIDEO','FILE','AUDIO') NOT NULL,
  url              VARCHAR(512) NOT NULL,   -- pointer para o objeto cifrado
  mime_type        VARCHAR(100) NULL,
  byte_size        BIGINT       NULL,
  sha256_hex       CHAR(64)     NULL,       -- verificação de integridade
  CONSTRAINT fk_ma_msg FOREIGN KEY (message_id) REFERENCES messages(id) ON DELETE CASCADE,
  INDEX ix_ma_msg (message_id)
) ENGINE=InnoDB;

-- STATUS por usuário (lido/entregue/etc.) — mantém
CREATE TABLE message_status (
  message_id BIGINT NOT NULL,
  user_id    BIGINT NOT NULL,
  status     ENUM('SENT','DELIVERED','READ','DELETED','FAILED') NOT NULL,
  timestamp  DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  PRIMARY KEY (message_id, user_id),              -- composto (dispensa id artificial)
  CONSTRAINT fk_ms_msg  FOREIGN KEY (message_id) REFERENCES messages(id) ON DELETE CASCADE,
  CONSTRAINT fk_ms_user FOREIGN KEY (user_id)    REFERENCES users(id),
  INDEX ix_ms_msg_status_ts (message_id, status, timestamp)
) ENGINE=InnoDB;


-- CREATE TABLE message_texts (
--     message_id BIGINT PRIMARY KEY,
--     content TEXT NULL,
--     FOREIGN KEY (message_id) REFERENCES messages(id) ON DELETE CASCADE
-- ) ENGINE=InnoDB;
