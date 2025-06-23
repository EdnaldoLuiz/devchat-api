package com.ednaldoluiz.websocket.domain.model.signal;

import com.ednaldoluiz.websocket.domain.model.base.TimestampedEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_signal_keys", schema = "websocket")
@Getter 
@Setter
public class UserSignalKeys extends TimestampedEntityBase {
    
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "registration_id", nullable = false)
    private int registrationId;

    @Lob
    @Column(name = "identity_key", nullable = false)
    private byte[] identityKey;

    @Column(name = "signed_pre_key_id", nullable = false)
    private int signedPreKeyId;

    @Lob
    @Column(name = "signed_pre_key", nullable = false)
    private byte[] signedPreKey;

    @Lob
    @Column(name = "signed_pre_sig", nullable = false)
    private byte[] signedPreSig;

}
