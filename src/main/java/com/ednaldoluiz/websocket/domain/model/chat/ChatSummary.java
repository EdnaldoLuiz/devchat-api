package com.ednaldoluiz.websocket.domain.model.chat;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "chat_summaries")
@Getter 
@Setter
@Builder
@NoArgsConstructor 
@AllArgsConstructor
@IdClass(ChatSummaryId.class)
public class ChatSummary {

    @Id 
    private Long userId;

    @Id 
    private Long chatId;

    private Long participantId;
    private String participantName;
    private String participantAvatar;

    private Long roomId;

    private Long  lastMessageId;
    private LocalDateTime lastMessageAt;
    private Long  lastMessageSenderId;

    @Column(columnDefinition = "TEXT") 
    private String lastMessageContent;

    private int unreadCount;
    
}