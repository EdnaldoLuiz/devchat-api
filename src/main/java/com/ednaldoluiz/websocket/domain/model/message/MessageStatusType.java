package com.ednaldoluiz.websocket.domain.model.message;

import lombok.Getter;

/**
 * Enumeração para os status de uma mensagem.
 * 
 * DELIVERED: Mensagem entregue.
 * READ: Mensagem lida.
 * DELETED: Mensagem deletada.
 * FAILED: Mensagem falhou.
 * PENDING: Mensagem pendente.
 */

@Getter
public enum MessageStatusType {

    DELIVERED,
    READ,
    DELETED,
    FAILED,
    PENDING
    
}
