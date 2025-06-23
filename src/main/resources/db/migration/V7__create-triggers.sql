/* ------------------------------------------------------------------
   TABELA MATERIALIZADA DE RESUMOS
   ------------------------------------------------------------------*/
CREATE TABLE chat_summaries (
    user_id                 BIGINT  NOT NULL,
    chat_id                 BIGINT  NOT NULL,
    -- chats PRIVADOS
    participant_id          BIGINT  NULL,
    -- ROOMS / GRUPOS
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

/* FK’s?  🔎  Para alto throughput não colocamos — evitamos
   lock de chave estrangeira em hot-table.  Consistência é
   garantida pelas triggers.  Se preferir, adicione:
   FOREIGN KEY (chat_id) REFERENCES chats(id) ON DELETE CASCADE
*/

/* ------------------------------------------------------------------
   TRIGGER #1 – cria linha no resumo quando o utilizador entra
   ------------------------------------------------------------------*/
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
        NEW.user_id,
        NEW.chat_id,
        IF(chat_kind = 'GROUP', NEW.chat_id, NULL)
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
           SET participant_id     = other_id,
               participant_name   = (SELECT name   FROM users WHERE id = other_id),
               participant_avatar = (SELECT avatar FROM users WHERE id = other_id)
         WHERE user_id = NEW.user_id
           AND chat_id = NEW.chat_id;

        -- Garante que o outro lado também tenha summary
        IF other_id IS NOT NULL THEN
            INSERT IGNORE INTO chat_summaries (
                user_id, chat_id, participant_id, participant_name, participant_avatar
            ) VALUES (
                other_id,
                NEW.chat_id,
                NEW.user_id,
                (SELECT name   FROM users WHERE id = NEW.user_id),
                (SELECT avatar FROM users WHERE id = NEW.user_id)
            );
        END IF;
    END IF;
END;

/* ------------------------------------------------------------------
   TRIGGER #2 – nova mensagem (atualiza last + unread)
   ------------------------------------------------------------------*/
CREATE TRIGGER trg_msg_insert_summary
    AFTER INSERT ON messages
    FOR EACH ROW
BEGIN
    /* 2-a) atualiza linha de quem ENVIOU (mantém unread = 0) -------*/
    UPDATE chat_summaries
       SET last_message_id        = NEW.id,
           last_message_at        = NEW.sent_at,
           last_message_sender_id = NEW.user_id,
           last_message_content   = (
               SELECT content
                 FROM message_texts
                WHERE message_id = NEW.id
           )
     WHERE user_id = NEW.user_id
       AND chat_id = NEW.chat_id;

    /* 2-b) incrementa unread dos OUTROS participantes --------------*/
    UPDATE chat_summaries
       SET last_message_id        = NEW.id,
           last_message_at        = NEW.sent_at,
           last_message_sender_id = NEW.user_id,
           last_message_content   = (
               SELECT content
                 FROM message_texts
                WHERE message_id = NEW.id
           ),
           unread_count           = unread_count + 1
     WHERE chat_id = NEW.chat_id
       AND user_id <> NEW.user_id;
END;

/* ------------------------------------------------------------------
   TRIGGER #3 – mudança de status → baixa contador de unread
   ------------------------------------------------------------------*/
CREATE TRIGGER trg_status_update_summary
    AFTER UPDATE ON message_status
    FOR EACH ROW
BEGIN
    /* só quando saímos de PENDING → DELIVERED/READ */
    IF OLD.status = 'PENDING' AND NEW.status IN ('DELIVERED','READ') THEN
        UPDATE chat_summaries
           SET unread_count = GREATEST(unread_count - 1, 0)
         WHERE user_id = NEW.user_id
           AND chat_id = (
               SELECT chat_id
                 FROM messages
                WHERE id = NEW.message_id
           );
    END IF;
END;
