package com.ednaldoluiz.websocket.web.controller.v1.chat;

import com.ednaldoluiz.websocket.domain.model.chat.Chat;
import com.ednaldoluiz.websocket.domain.model.chat.ChatType;
import com.ednaldoluiz.websocket.domain.model.chat.UsersChat;
import com.ednaldoluiz.websocket.domain.model.chat.ChatStatus;
import com.ednaldoluiz.websocket.domain.model.message.Message;
import com.ednaldoluiz.websocket.domain.model.message.MessageText;
import com.ednaldoluiz.websocket.domain.model.message.MessageStatus;
import com.ednaldoluiz.websocket.domain.model.message.MessageStatusType;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.ChatRepository;
import com.ednaldoluiz.websocket.infra.persistence.MessageRepository;
import com.ednaldoluiz.websocket.infra.persistence.MessageTextRepository;
import com.ednaldoluiz.websocket.infra.persistence.MessageStatusRepository;
import com.ednaldoluiz.websocket.infra.persistence.UserRepository;
import com.ednaldoluiz.websocket.infra.persistence.UsersChatsRepository;
import com.ednaldoluiz.websocket.web.controller.v1.chat.ChatMessage;
import com.ednaldoluiz.websocket.web.websocket.store.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate template;
    private final UserRepository userRepo;
    private final ChatRepository chatRepo;
    private final UsersChatsRepository ucRepo;
    private final MessageRepository msgRepo;
    private final MessageTextRepository textRepo;
    private final MessageStatusRepository statusRepo;

    /**
     * Envia mensagem privada de um usuário para outro.
     */
    @MessageMapping("/chat.private.{toEmail}")
    @Transactional
    public void direct(
            @DestinationVariable String toEmail,
            ChatMessage msgDto,
            Principal principal
    ) {
        // 1) Extrai usuário autenticado
        AuthUser authUser = (AuthUser) ((org.springframework.security.core.Authentication) principal).getPrincipal();
        log.info("Usuário autenticado: {}", authUser);

        // 2) Carrega entidades User (evita proxy hibernado desconectado)
        User fromUser = userRepo.findById(authUser.id())
                .orElseThrow(() -> new IllegalArgumentException("Remetente não encontrado"));
        User toUser = userRepo.findByEmail(toEmail)
                .orElseThrow(() -> new IllegalArgumentException("Destinatário não encontrado"));

        // 3) Busca ou cria Chat PRIVATE entre os dois
        List<Long> ids = Arrays.asList(fromUser.getId(), toUser.getId());
        Collections.sort(ids);
        Chat chat = chatRepo.findPrivateBetween(ids.get(0), ids.get(1))
                .orElseGet(() -> createPrivateChat(fromUser, toUser));

        // 4) Persiste a mensagem
        Message message = new Message();
        message.setChat(chat);
        message.setUser(fromUser);
        message.setMessageUuid(msgDto.getMessageUuid());
        message.setSentAt(LocalDateTime.now());
        message.setDeleted(false);
        message = msgRepo.save(message);

        // 5) Persiste o texto da mensagem
        MessageText mt = new MessageText();
        mt.setMessage(message);
        mt.setContent(msgDto.getContent());
        textRepo.save(mt);

        // 6) Status para remetente: DELIVERED
        MessageStatus stFrom = new MessageStatus();
        stFrom.setMessage(message);
        stFrom.setUser(fromUser);
        stFrom.setStatus(MessageStatusType.DELIVERED);
        stFrom.setTimestamp(LocalDateTime.now());
        statusRepo.save(stFrom);

        // 7) Status para destinatário: PENDING
        MessageStatus stTo = new MessageStatus();
        stTo.setMessage(message);
        stTo.setUser(toUser);
        stTo.setStatus(MessageStatusType.PENDING);
        stTo.setTimestamp(LocalDateTime.now());
        statusRepo.save(stTo);

        // 8) Monta DTO de saída e envia
        ChatMessage out = new ChatMessage();
        out.setId(message.getId().toString());
        out.setChatId(chat.getId().toString());
        out.setSenderEmail(fromUser.getEmail());
        out.setMessageUuid(message.getMessageUuid());
        out.setRecipientEmail(toUser.getEmail());
        out.setContent(msgDto.getContent());
        out.setType(msgDto.getType());
        long epochMillis = message.getSentAt()
                .atZone(ZoneOffset.UTC)
                .toInstant()
                .toEpochMilli();
        out.setTimestamp(epochMillis);

        template.convertAndSendToUser(fromUser.getEmail(), "/queue/messages", out);
        template.convertAndSendToUser(toUser.getEmail(),    "/queue/messages", out);

        log.info("Mensagem de {} para {} enviada (chat {})", fromUser.getEmail(), toUser.getEmail(), chat.getId());
    }

    /**
     * Cria um chat privado e vincula os usuários.
     */
    private Chat createPrivateChat(User a, User b) {
        Chat c = new Chat();
        c.setType(ChatType.PRIVATE);
        c.setName(String.format("Chat privado: %s & %s", a.getName(), b.getName()));
        c = chatRepo.save(c);

        UsersChat ucA = new UsersChat();
        ucA.setChat(c);
        ucA.setUser(a);
        ucA.setStatus(ChatStatus.ACTIVE);

        UsersChat ucB = new UsersChat();
        ucB.setChat(c);
        ucB.setUser(b);
        ucB.setStatus(ChatStatus.ACTIVE);

        ucRepo.saveAll(Arrays.asList(ucA, ucB));
        return c;
    }
}
