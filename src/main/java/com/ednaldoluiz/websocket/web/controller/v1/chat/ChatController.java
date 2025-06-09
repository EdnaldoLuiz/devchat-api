package com.ednaldoluiz.websocket.web.controller.v1.chat;

import com.ednaldoluiz.websocket.domain.model.chat.*;
import com.ednaldoluiz.websocket.domain.model.message.*;
import com.ednaldoluiz.websocket.domain.model.notification.NotificationType;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.repository.*;
import com.ednaldoluiz.websocket.web.websocket.store.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate template;
    private final UserRepository        userRepo;
    private final ChatRepository        chatRepo;
    private final UsersChatsRepository  ucRepo;
    private final MessageRepository     msgRepo;
    private final MessageTextRepository textRepo;
    private final MessageStatusRepository statusRepo;

    /* ------------------------------------------------------------------ *
     * 1) MENSAGEM PRIVADA (/chat.private.{toEmail})
     * ------------------------------------------------------------------ */
    @MessageMapping("/chat.privates.{toEmail}")
    @Transactional
    public void handlePrivateMessage(
            @DestinationVariable String toEmail,
            ChatMessage msgDto,
            Principal principal
    ) {
        log.info(">>> TYPING to /user/queue/typing for {}", toEmail);
        AuthUser auth = auth(principal);
        User fromUser = userEntity(auth.id());
        User toUser   = userRepo.findByEmail(toEmail)
                .orElseThrow(() -> new IllegalArgumentException("Destinatário não encontrado"));

        Chat chat = getOrCreatePrivateChat(fromUser, toUser);

        /* persiste message ------------------------------------------------ */
        Message message = new Message();
        message.setChat(chat);
        message.setUser(fromUser);
        message.setMessageUuid(msgDto.messageUuid());
        message.setSentAt(LocalDateTime.now());
        msgRepo.save(message);

        MessageText text = new MessageText();
        text.setMessage(message);
        text.setContent(msgDto.content());
        textRepo.save(text);

        saveStatus(message, fromUser, MessageStatusType.SENT);
        saveStatus(message, toUser,   MessageStatusType.DELIVERED);

        ChatMessage out = new ChatMessage(
                message.getId().toString(),
                chat.getId().toString(),
                msgDto.messageUuid(),
                fromUser.getEmail(),
                toUser.getEmail(),
                msgDto.content(),
                msgDto.type(),
                message.getSentAt().atZone(ZoneOffset.UTC).toInstant().toEpochMilli()
        );

        sendToUsers(out, fromUser.getEmail(), toUser.getEmail());
        log.info("MSG {} -> {} (chat {})", fromUser.getEmail(), toUser.getEmail(), chat.getId());
    }


    /* ------------------------------------------------------------------ *
     * 3) LIDO (/chat.read.{chatId})
     * ------------------------------------------------------------------ */
    @MessageMapping("/chat.read.{chatId}")
    @Transactional
    public void handleReadReceipt(
            @DestinationVariable Long chatId,
            ReadReceiptDto receipt,
            Principal principal
    ) {
        User reader = userEntity(auth(principal).id());

        // marca todas as pendentes como READ para esse usuário
        statusRepo.markChatMessagesAsRead(
                chatId,
                reader.getId(),
                LocalDateTime.now(),
                MessageStatusType.SENT,
                MessageStatusType.READ
        );

        template.convertAndSendToUser(
                receipt.otherEmail(),
                "/queue/read",
                receipt
        );
        log.debug("READ chat:{} by {}", chatId, reader.getEmail());
    }

    /* ------------------------------------------------------------------ *
     * 4) NOTIFICAÇÕES PUSH (/chat.notify)
     * ------------------------------------------------------------------ */
    @MessageMapping("/chat.notify")
    public void handleNotification(NotificationDto dto, Principal principal) {
        // broadcast genérico – hoje só loga
        template.convertAndSendToUser(dto.toEmail(), "/queue/notify", dto);
        log.debug("NOTIFY {} -> {}", auth(principal).email(), dto.toEmail());
    }

    /* ------------------------------------------------------------------ *
     * 5) MENSAGEM DE ARQUIVO (/chat.file.{toEmail})
     *    (aqui só faz broadcast; upload real continua num endpoint REST)
     * ------------------------------------------------------------------ */
    @MessageMapping("/chat.file.{toEmail}")
    public void handleFileMessage(
            @DestinationVariable String toEmail,
            FileMessageDto dto,
            Principal principal
    ) {
        template.convertAndSendToUser(toEmail, "/queue/files", dto);
        template.convertAndSendToUser(auth(principal).email(), "/queue/files", dto);
        log.info("FILE {} -> {} ({})", auth(principal).email(), toEmail, dto.fileName());
    }

    @MessageMapping("/chat.typings.{to}")
    public void typing(
        @DestinationVariable String to,
        @Payload Map<String,String> body
    ){
        template.convertAndSendToUser(to, "/queue/typing", body);
    }

    /* ------------------------------------------------------------------ *
     * 6) PRESENÇA ONLINE (/chat.presence.{chatId})
     * ------------------------------------------------------------------ */
    @MessageMapping("/chat.presence.{chatId}")
    public void handlePresence(
            @DestinationVariable Long chatId,
            PresenceEventDto dto,
            Principal principal
    ) {
        // envia para todos do chat exceto o remetente
        chatRepo.membersEmails(chatId).stream()
                .filter(email -> !email.equals(dto.email()))
                .forEach(email -> template.convertAndSendToUser(email, "/queue/presence", dto));

        log.debug("PRESENCE {} in chat {}", dto.status(), chatId);
    }

    /* ================================================================== *
     * ---------------------- HELPERS / UTIL ----------------------------- *
     * ================================================================== */
    private AuthUser auth(Principal p) {
        return (AuthUser) ((Authentication) p).getPrincipal();
    }
    private User userEntity(Long id) {
        return userRepo.findById(id).orElseThrow();
    }
    private void sendToUsers(Object payload, String... emails) {
        for (String email : emails) {
            template.convertAndSendToUser(email, "/queue/messages", payload);
        }
    }
    private void saveStatus(Message m, User u, MessageStatusType type) {
        MessageStatus st = new MessageStatus();
        st.setMessage(m);
        st.setUser(u);
        st.setStatus(type);
        st.setTimestamp(LocalDateTime.now());
        statusRepo.save(st);
    }
    private Chat getOrCreatePrivateChat(User a, User b) {
        List<Long> ids = Arrays.asList(a.getId(), b.getId());
        Collections.sort(ids);
        return chatRepo.findPrivateBetween(ids.get(0), ids.get(1))
                .orElseGet(() -> {
                    Chat c = new Chat();
                    c.setType(ChatType.PRIVATE);
                    c.setName(a.getName() + " & " + b.getName());
                    chatRepo.save(c);
                    ucRepo.saveAll(List.of(
                            usersChat(c, a), usersChat(c, b)
                    ));
                    return c;
                });
    }
    private UsersChat usersChat(Chat c, User u) {
        UsersChat uc = new UsersChat();
        uc.setChat(c);
        uc.setUser(u);
        uc.setStatus(ChatStatus.ACTIVE);
        return uc;
    }

    /* ================================================================== *
     * ---------------------------  DTOs  -------------------------------- *
     * ================================================================== */
    // 📩 conteúdo de texto

    // ✍️ typing
    public record TypingEvent(
            String fromEmail,
            String toEmail
    ) {}

    // 📬 recibo de leitura
    public record ReadReceiptDto(
            String chatId,
            String otherEmail,
            long   lastMessageTimestamp
    ) {}

    // 📢 notificações genéricas
    public record NotificationDto(
            String toEmail,
            NotificationType type,
            String fromEmail
    ) {}

    // 📥 mensagem de arquivo (só metadados)
    public record FileMessageDto(
            String fileUuid,
            String senderEmail,
            String recipientEmail,
            String fileName,
            long   sizeBytes,
            String mimeType,
            long   timestamp
    ) {}

    // 🧍 presença
    public record PresenceEventDto(
            String email,
            String status   // e.g. ONLINE, OFFLINE, AWAY
    ) {}
}
 