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
    INDEX idx_user_notification (user_id, read_at),
    CHECK (type = 'MESSAGE' AND message_id IS NOT NULL OR type != 'MESSAGE')
) ENGINE = InnoDB;