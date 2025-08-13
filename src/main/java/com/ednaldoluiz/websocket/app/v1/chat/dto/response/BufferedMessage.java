package com.ednaldoluiz.websocket.app.v1.chat.dto.response;

import java.io.Serializable;
import java.util.UUID;

import com.ednaldoluiz.websocket.app.v1.chat.command.AttachmentMessageCommand;
import com.ednaldoluiz.websocket.app.v1.chat.command.SendMessageCommand;
import com.ednaldoluiz.websocket.app.v1.chat.command.TextMessageCommand;
import com.ednaldoluiz.websocket.domain.model.chat.Chat;
import com.ednaldoluiz.websocket.domain.model.message.MessageAttachmentType;
import com.ednaldoluiz.websocket.domain.model.user.User;

public record BufferedMessage(
        Long   chatId,
        UUID   messageUuid,
        Long   fromId,
        Long   toId,
        int    cipherType,        // <-- NOVO
        String cipherBodyB64,     // <-- em vez de payloadBase64
        boolean hasAttachment,
        MessageAttachmentType attachmentType,
        String attachmentUrl,
        long   sentAtMillis
) implements Serializable {

    public static BufferedMessage of(
            User from, User to, Chat chat, SendMessageCommand cmd, byte[] cipherBody) {

        // descobre se tem attach
        boolean attach = cmd instanceof AttachmentMessageCommand;
        MessageAttachmentType attType = attach ? ((AttachmentMessageCommand) cmd).type() : null;
        String attUrl   = attach ? ((AttachmentMessageCommand) cmd).url()  : null;
        int cipherType  = switch (cmd) {
            case TextMessageCommand t       -> t.content().type();
            case AttachmentMessageCommand a -> a.content().type();
            default                         -> 1; // Whisper padrão
        };

        return new BufferedMessage(
                chat.getId(),
                cmd.messageUuid(),
                from.getId(),
                to.getId(),
                cipherType,
                java.util.Base64.getEncoder().encodeToString(cipherBody),
                attach,
                attType,
                attUrl,
                System.currentTimeMillis());
    }
}

