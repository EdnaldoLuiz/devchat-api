package com.ednaldoluiz.websocket.app.v1.auth.validator.chat.usecase.adapter;

import org.springframework.stereotype.Component;

import com.ednaldoluiz.websocket.app.v1.auth.validator.chat.usecase.port.StartPrivateChatUseCasePort;
import com.ednaldoluiz.websocket.domain.model.chat.Chat;
import com.ednaldoluiz.websocket.domain.model.chat.ChatStatus;
import com.ednaldoluiz.websocket.domain.model.chat.ChatType;
import com.ednaldoluiz.websocket.domain.model.chat.UsersChat;
import com.ednaldoluiz.websocket.domain.model.user.User;
import com.ednaldoluiz.websocket.infra.persistence.ChatRepository;
import com.ednaldoluiz.websocket.infra.persistence.UserRepository;
import com.ednaldoluiz.websocket.infra.persistence.UsersChatsRepository;
import com.ednaldoluiz.websocket.shared.generator.SnowflakeIdGenerator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class StartPrivateChatUseCaseAdapter implements StartPrivateChatUseCasePort {
    
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final UsersChatsRepository usersChatsRepository; // ou algo assim
    private final SnowflakeIdGenerator idGenerator;

    @Override
    public Long startPrivateChat(Long userId1, Long userId2) {
        Chat chat = new Chat(idGenerator);
        chat.setName("Chat Privado entre "+userId1+" e "+userId2);
        chat.setType(ChatType.PRIVATE);
        chatRepository.save(chat);

        // Criar UsersChats pra cada usuário
        User u1 = userRepository.findById(userId1).orElseThrow();
        User u2 = userRepository.findById(userId2).orElseThrow();

        UsersChat uc1 = new UsersChat(idGenerator);
        uc1.setUser(u1);
        uc1.setChat(chat);
        uc1.setStatus(ChatStatus.ACTIVE);
        usersChatsRepository.save(uc1);

        UsersChat uc2 = new UsersChat(idGenerator);
        uc2.setUser(u2);
        uc2.setChat(chat);
        uc2.setStatus(ChatStatus.ACTIVE);
        usersChatsRepository.save(uc2);

        return chat.getId();
    }
}