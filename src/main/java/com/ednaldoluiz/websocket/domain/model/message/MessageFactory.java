package com.ednaldoluiz.websocket.domain.model.message;

import java.util.UUID;

import com.ednaldoluiz.websocket.domain.model.chat.Chat;
import com.ednaldoluiz.websocket.domain.model.user.User;

public final class MessageFactory {

    private MessageFactory() {}

    public static Message withText(Chat chat, User from, UUID uuid, String content) {
        Message message = base(chat, from, uuid);
        if (content != null && !content.isBlank()) {
            message.addText(content);
        }
        return message;
    }

    public static Message withAttachment(Chat chat, User from, UUID uuid,
                                         String content,
                                         MessageAttachmentType type,
                                         String url) {
        Message m = withText(chat, from, uuid, content);
        m.addAttachment(type, url);         // outro método de domínio
        return m;
    }

    /* ---------- helpers ---------- */
    private static Message base(Chat chat, User from, UUID uuid) {
        return new Message(uuid, chat, from);   // construtor protegido
    }
}