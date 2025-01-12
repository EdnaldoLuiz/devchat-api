package com.ednaldoluiz.websocket.app.v1.usecase.adapter;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.app.v1.usecase.dto.request.SendMessageRequest;
import com.ednaldoluiz.websocket.app.v1.usecase.dto.response.SendMessageResponse;
import com.ednaldoluiz.websocket.app.v1.usecase.port.SendMessageUseCasePort;
import com.ednaldoluiz.websocket.domain.model.chat.Chat;
import com.ednaldoluiz.websocket.domain.model.message.Message;
import com.ednaldoluiz.websocket.domain.model.message.MessageStatus;
import com.ednaldoluiz.websocket.domain.model.message.MessageStatusType;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.ChatRepository;
import com.ednaldoluiz.websocket.infra.persistence.MessageRepository;
import com.ednaldoluiz.websocket.infra.persistence.MessageStatusRepository;
import com.ednaldoluiz.websocket.infra.persistence.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendMessageUseCaseAdapter implements SendMessageUseCasePort {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final MessageStatusRepository messageStatusRepository;

    @Override
    @Transactional
    public SendMessageResponse sendMessage(SendMessageRequest request) {
        log.info("Enviando mensagem: {}", request.message());

        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Chat chat = chatRepository.findById(request.chatId())
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        Message savedMessage = messageRepository.save(
            new Message(request, user, chat));

        MessageStatus messageStatus = new MessageStatus();
        messageStatus.setMessage(savedMessage);
        messageStatus.setUser(user);
        messageStatus.setStatus(MessageStatusType.PENDING);
        messageStatus.setTimestamp(LocalDateTime.now());
        messageStatusRepository.save(messageStatus);

        log.info("Mensagem enviada: {}", request.message());
        return new SendMessageResponse(savedMessage.getId(), "Message sent successfully.");
    }
}
