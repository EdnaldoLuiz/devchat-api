-- Criação da tabela messages
-- Tabela principal de mensagens
-- A) Tabela de mensagens
CREATE TABLE messages (
    id BIGINT PRIMARY KEY,
    chat_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
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

-- C) Anexos (somente para type=MEDIA)
CREATE TABLE message_attachments (
    id BIGINT PRIMARY KEY,
    message_id BIGINT NOT NULL,
    attachment_type ENUM('IMAGE','VIDEO','FILE','AUDIO') NOT NULL,
    url VARCHAR(512) NOT NULL,
    attachment_data MEDIUMBLOB,
    FOREIGN KEY (message_id) REFERENCES messages(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Criação da tabela notifications
CREATE TABLE notifications (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    message_id BIGINT,
    type ENUM('MESSAGE', 'GROUP_INVITE') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    read_at TIMESTAMP NULL,
    deleted BOOLEAN DEFAULT FALSE,
    CONSTRAINT fk_user_notification FOREIGN KEY (user_id) REFERENCES users (id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_message_notification FOREIGN KEY (message_id) REFERENCES messages (id) 
        ON DELETE CASCADE,
    INDEX idx_user_notification (user_id, read_at)
    CHECK (type = 'MESSAGE' AND message_id IS NOT NULL OR type != 'MESSAGE')
) ENGINE = InnoDB;

-- Criação da tabela message_status
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