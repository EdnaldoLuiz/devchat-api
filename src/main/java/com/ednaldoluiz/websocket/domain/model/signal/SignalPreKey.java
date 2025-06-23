package com.ednaldoluiz.websocket.domain.model.signal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "signal_one_time_pre_keys", schema = "websocket")
@Getter 
@Setter 
@EqualsAndHashCode(of = {"userId","keyId"})
@IdClass(SignalPreKeyId.class)
public class SignalPreKey {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Id
    @Column(name = "key_id", nullable = false, updatable = false)
    private int keyId;

    @Lob
    @Column(name = "pre_key", nullable = false)
    private byte[] preKey;

    @Column(name = "consumed", nullable = false)
    private boolean consumed;

    public SignalPreKey() {}
    
    public SignalPreKey(Long userId, int keyId, byte[] preKey, boolean consumed) {
        this.userId = userId;
        this.keyId = keyId;
        this.preKey = preKey;
        this.consumed = consumed;
    }
}