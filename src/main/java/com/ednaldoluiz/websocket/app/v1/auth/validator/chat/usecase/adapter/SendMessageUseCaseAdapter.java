package com.ednaldoluiz.websocket.app.v1.auth.validator.chat.usecase.adapter;

import java.time.LocalDateTime;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.app.v1.auth.validator.chat.usecase.port.SendMessageUseCasePort;
import com.ednaldoluiz.websocket.domain.model.chat.Chat;
import com.ednaldoluiz.websocket.domain.model.message.Message;
import com.ednaldoluiz.websocket.domain.model.message.MessageText;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.ChatRepository;
import com.ednaldoluiz.websocket.infra.persistence.MessageRepository;
import com.ednaldoluiz.websocket.infra.persistence.UserRepository;
import com.ednaldoluiz.websocket.shared.generator.SnowflakeIdGenerator;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class SendMessageUseCaseAdapter implements SendMessageUseCasePort {

    private final MessageRepository messageRepository;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final SnowflakeIdGenerator idGenerator;
    private final SimpMessagingTemplate simpMessagingTemplate; // do Spring p/ websocket

    @Override
    @Transactional
    public void sendMessage(Long chatId, Long senderId, String content) {
        // Busca o Chat e o User pelo ID
        Chat chat = chatRepository.findById(chatId)
            .orElseThrow(() -> new RuntimeException("Chat não encontrado"));
        User user = userRepository.findById(senderId)
            .orElseThrow(() -> new RuntimeException("User não encontrado"));

        // Cria/Salva Message
        Message message = new Message(idGenerator);
        message.setChat(chat);
        message.setUser(user);
        // Cria e associa o MessageText com o conteúdo da mensagem
        MessageText messageText = new MessageText(content);
        message.setMessageText(messageText);
        messageText.setMessage(message);
        messageRepository.save(message);

        // Envia via websocket para os inscritos no chat
        simpMessagingTemplate.convertAndSend(
            "/topic/chat/" + chatId, 
            message // ou converta para um DTO adequado
        );
    }
}