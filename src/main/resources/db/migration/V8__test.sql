DROP TRIGGER IF EXISTS trg_uc_insert_summary;

DELIMITER $$

CREATE TRIGGER trg_uc_insert_summary
    AFTER INSERT ON users_chats
    FOR EACH ROW
BEGIN
    DECLARE chat_kind ENUM('PRIVATE','GROUP');
    DECLARE other_id  BIGINT;

    -- Pega o tipo do chat (PRIVATE ou GROUP)
    SELECT type INTO chat_kind
    FROM chats
    WHERE id = NEW.chat_id;

    -- Sempre tenta inserir o summary básico (GROUP ou PRIVATE inicial)
    INSERT IGNORE INTO chat_summaries (
        user_id, chat_id, room_id
    ) VALUES (
                 NEW.user_id, NEW.chat_id, IF(chat_kind = 'GROUP', NEW.chat_id, NULL)
             );

    -- Se for um chat privado, completa os dados
    IF chat_kind = 'PRIVATE' THEN
        -- Busca o outro participante, se já existir
        SELECT user_id INTO other_id
        FROM users_chats
        WHERE chat_id = NEW.chat_id
          AND user_id <> NEW.user_id
        LIMIT 1;

        -- Atualiza os campos do resumo do usuário atual
        UPDATE chat_summaries
        SET participant_id = other_id,
            participant_name = (SELECT name FROM users WHERE id = other_id),
            participant_avatar = (SELECT avatar FROM users WHERE id = other_id)
        WHERE user_id = NEW.user_id
          AND chat_id = NEW.chat_id;

        -- Garante que o outro lado também tenha summary
        IF other_id IS NOT NULL THEN
            INSERT IGNORE INTO chat_summaries (
                user_id, chat_id, participant_id, participant_name, participant_avatar
            )
            VALUES (
                       other_id, NEW.chat_id, NEW.user_id,
                       (SELECT name FROM users WHERE id = NEW.user_id),
                       (SELECT avatar FROM users WHERE id = NEW.user_id)
                   );
        END IF;
    END IF;
END$$

DELIMITER ;
