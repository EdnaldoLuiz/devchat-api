package com.ednaldoluiz.websocket.domain.model.signal;

import java.io.Serializable;
import lombok.*;

@Getter 
@Setter 
@NoArgsConstructor 
@AllArgsConstructor
public class SignalPreKeyId implements Serializable {

    private Long userId;
    private int keyId;

}