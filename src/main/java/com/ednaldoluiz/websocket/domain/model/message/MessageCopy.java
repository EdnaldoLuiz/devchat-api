package com.ednaldoluiz.websocket.domain.model.message;

import java.time.LocalDateTime;

import com.ednaldoluiz.websocket.domain.model.base.EntityBase;
import com.ednaldoluiz.websocket.domain.model.chat.Chat;
import com.ednaldoluiz.websocket.domain.model.user.User;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter 
@Setter
@Entity(name = "MessageCopy")
@Table(name = "message_copies", schema = "websocket",
       indexes = {
         @Index(name="ix_mc_chat_target_ts", columnList = "chat_id,target_user_id,sent_at"),
         @Index(name="ix_mc_chat_ts", columnList = "chat_id,sent_at")
       })
public class MessageCopy extends EntityBase {

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="chat_id", nullable=false)
  private Chat chat;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="message_id", nullable=false)
  private Message message;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name="target_user_id", nullable=false)
  private User targetUser;

  @Convert(converter = CipherTypeConverter.class)
  @Column(name="cipher_type", nullable=false)
  private CipherType cipherType;

  @Lob
  @Column(name="cipher_body", nullable=false, columnDefinition = "MEDIUMBLOB")
  private byte[] cipherBody;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name="sent_at", nullable=false)
  private LocalDateTime sentAt;

  public static MessageCopy of(Message message, Chat chat, User target, CipherType type, byte[] body) {
    MessageCopy messageCopy = new MessageCopy();
    messageCopy.message = message;
    messageCopy.chat = chat;
    messageCopy.targetUser = target;
    messageCopy.cipherType = type;
    messageCopy.cipherBody = body;
    messageCopy.sentAt = message.getSentAt();
    return messageCopy;
  }

}
