CREATE TABLE messages (
    id BIGINT PRIMARY KEY,
    chat_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    message_uuid BINARY(16) UNIQUE NULL,
    sent_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (chat_id) REFERENCES chats(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE TABLE message_texts (
    message_id BIGINT PRIMARY KEY,
    content TEXT NOT NULL,
    FOREIGN KEY (message_id) REFERENCES messages(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE message_attachments (
    id BIGINT PRIMARY KEY,
    message_id BIGINT NOT NULL,
    attachment_type ENUM('IMAGE','VIDEO','FILE','AUDIO') NOT NULL,
    url VARCHAR(512) NOT NULL,
    attachment_data MEDIUMBLOB,
    FOREIGN KEY (message_id) REFERENCES messages(id) ON DELETE CASCADE,
    CHECK (attachment_data IS NOT NULL OR CHAR_LENGTH(url) > 0)
) ENGINE=InnoDB;

CREATE TABLE message_status (
    id BIGINT PRIMARY KEY,
    message_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    status ENUM('DELIVERED', 'READ', 'DELETED', 'FAILED', 'PENDING') NOT NULL,
    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_message_status FOREIGN KEY (message_id) REFERENCES messages (id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_user_status FOREIGN KEY (user_id) REFERENCES users (id) 
        ON DELETE CASCADE,
    UNIQUE (message_id, user_id),
    INDEX idx_message_status (message_id, status, timestamp)
) ENGINE = InnoDB;