package com.ednaldoluiz.websocket.app.v1.chat.dto.response;

import com.ednaldoluiz.websocket.app.v1.chat.command.AttachmentMessageCommand;
import com.ednaldoluiz.websocket.app.v1.chat.command.SendMessageCommand;
import com.ednaldoluiz.websocket.app.v1.chat.command.TextMessageCommand;
import com.ednaldoluiz.websocket.domain.model.chat.Chat;
import com.ednaldoluiz.websocket.domain.model.message.MessageAttachmentType;
import com.ednaldoluiz.websocket.domain.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;
import java.util.UUID;

public record BufferedMessage(
        UUID messageUuid,
        Long fromId,
        Long toId,
        Long chatId,
        int  cipherType,      // 1 (Whisper) | 3 (PreKey)
        byte[] cipherBody,    // BYTES!
        boolean hasAttachment,
        MessageAttachmentType attachmentType,
        String attachmentUrl,
        long sentAtMillis
) {
    private static final Logger log = LoggerFactory.getLogger(BufferedMessage.class);

    public String cipherBodyB64() {
        return Base64.getEncoder().encodeToString(cipherBody);
    }

    /* Factory */
    public static BufferedMessage of(User from, User to, Chat chat, SendMessageCommand cmd, byte[] cipherBody) {
        final UUID uuid = cmd.messageUuid();
        final long now  = System.currentTimeMillis();

        final int ctype = cmd.content().type();

        boolean hasAttach = false;
        MessageAttachmentType attachType = null;
        String attachUrl = null;

        if (cmd instanceof AttachmentMessageCommand a) {
            hasAttach = true;
            attachType = a.type();
            attachUrl  = a.url();
        } else if (cmd instanceof TextMessageCommand) {
            // nada
        }

        BufferedMessage out = new BufferedMessage(
                uuid,
                from.getId(),
                to.getId(),
                chat.getId(),
                ctype,
                cipherBody,
                hasAttach,
                attachType,
                attachUrl,
                now
        );

        int first = cipherBody.length > 0 ? (cipherBody[0] & 0xFF) : -1;
        log.info("[BUFFER] uuid={} chat={} from={} to={} ctype={} bytes={} first=0x{} attach?{}",
                uuid, chat.getId(), from.getId(), to.getId(),
                ctype, cipherBody.length,
                first == -1 ? "??" : String.format("%02X", first),
                hasAttach);

        return out;
    }
}
