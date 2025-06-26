DELIMITER $$

CREATE PROCEDURE update_chat_summaries_after_batch()
BEGIN
    UPDATE chat_summaries cs
    JOIN (
        SELECT m.chat_id, MAX(m.id) AS last_msg_id, MAX(m.sent_at) AS last_sent_at
        FROM messages m
        GROUP BY m.chat_id
    ) latest ON latest.chat_id = cs.chat_id
    JOIN message_texts mt ON mt.message_id = latest.last_msg_id
    SET cs.last_message_id = latest.last_msg_id,
        cs.last_message_at = latest.last_sent_at,
        cs.last_message_content = mt.content,
        cs.last_message_sender_id = (SELECT user_id FROM messages WHERE id = latest.last_msg_id);
END$$

DELIMITER ;
