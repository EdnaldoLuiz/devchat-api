-- Criação da tabela chats
CREATE TABLE chats (
    id BIGINT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    type ENUM('PRIVATE', 'GROUP') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Criação da tabela users_chats
CREATE TABLE users_chats (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    chat_id BIGINT NOT NULL,
    status ENUM('ACTIVE', 'ARCHIVED', 'BLOCKED', 'MUTED') DEFAULT 'ACTIVE',
    last_read_message_id BIGINT DEFAULT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT fk_user_chat FOREIGN KEY (user_id) REFERENCES users (id) 
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_chat FOREIGN KEY (chat_id) REFERENCES chats (id) 
        ON DELETE CASCADE ON UPDATE CASCADE,
    UNIQUE (user_id, chat_id) 
);

CREATE TABLE chat_summaries (
    user_id                 BIGINT  NOT NULL,
    chat_id                 BIGINT  NOT NULL,
    participant_id          BIGINT  NULL,
    room_id                 BIGINT  NULL,
    participant_name        VARCHAR(120),
    participant_avatar      VARCHAR(255),
    last_message_id         BIGINT,
    last_message_at         TIMESTAMP,
    last_message_content    TEXT,
    last_message_sender_id  BIGINT,
    unread_count            INT     DEFAULT 0,
    PRIMARY KEY (user_id, chat_id),

    /* feed da sidebar = ORDER BY last_message_at DESC   */
    INDEX idx_user_lastmsg (user_id, last_message_at DESC)
);