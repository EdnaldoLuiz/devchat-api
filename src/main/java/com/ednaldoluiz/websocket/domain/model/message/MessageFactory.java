package com.ednaldoluiz.websocket.domain.model.message;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

import com.ednaldoluiz.websocket.app.v1.chat.command.AttachmentMessageCommand;
import com.ednaldoluiz.websocket.app.v1.chat.command.SendMessageCommand;
import com.ednaldoluiz.websocket.app.v1.chat.command.TextMessageCommand;
import com.ednaldoluiz.websocket.app.v1.chat.dto.response.BufferedMessage;
import com.ednaldoluiz.websocket.domain.model.chat.Chat;
import com.ednaldoluiz.websocket.domain.model.user.User;

public final class MessageFactory {

    private MessageFactory() {
    }

    public static Message createText(Chat chat, User sender, UUID uuid, byte[] cipherBody) {
        return new Message(uuid, chat, sender, cipherBody);
    }

    public static Message createWithAttachment(Chat chat, User sender, UUID uuid, byte[] cipherBody,
            MessageAttachmentType type, String url) {
        Message message = new Message(uuid, chat, sender, cipherBody);
        message.addAttachment(new MessageAttachments(message, type, url));
        return message;
    }

    public static Message fromCommand(Chat chat, User sender, SendMessageCommand cmd, byte[] cipherBody) {
        if (cmd instanceof AttachmentMessageCommand attachment) {
            return createWithAttachment(chat, sender, attachment.messageUuid(), cipherBody, attachment.type(),
                    attachment.url());
        } else if (cmd instanceof TextMessageCommand text) {
            return createText(chat, sender, text.messageUuid(), cipherBody);
        }
        throw new IllegalArgumentException("Message command desconhecido: " + cmd.getClass());
    }

    public static Message fromBuffered(BufferedMessage bm, Chat chat, User from, User to) {
        Message message = new Message(
                bm.messageUuid(),
                chat,
                from,
                Base64.getDecoder().decode(bm.cipherBodyB64()));
                message.setSentAt(Instant.ofEpochMilli(bm.sentAtMillis())
                        .atZone(java.time.ZoneId.systemDefault())
                        .toLocalDateTime()
                );
        if (bm.hasAttachment() && bm.attachmentType() != null && bm.attachmentUrl() != null) {
            message.addAttachment(new MessageAttachments(message, bm.attachmentType(), bm.attachmentUrl()));
        }
        return message;
    }
}