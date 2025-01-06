package com.ednaldoluiz.websocket.domain.model.message;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

/**
 * Enumeração para os tipos de anexos de mensagem.
 * 
 * IMAGE: Imagem.
 * VIDEO: Vídeo.
 * FILE: Arquivo.
 * AUDIO: Áudio.
 */

@Getter
public enum MessageAttachmentType {

    IMAGE("jpg", "jpeg", "png", "gif", "webp"),
    VIDEO("mp4", "webm", "mkv", "avi", "flv", "mov"),
    FILE("pdf", "doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "zip", "rar"),
    AUDIO("mp3", "wav");

    /**
     * Extensões de arquivo suportadas por cada tipo de anexo.
     */
    private final Set<String> extensions;

    MessageAttachmentType(String... extensions) {
        this.extensions = new HashSet<>(Arrays.asList(extensions));
    }

    /**
     * Verifica se uma extensão de arquivo pertence a este tipo de anexo.
     */
    public boolean supportsExtension(String extension) {
        return extensions.contains(extension.toLowerCase());
    }

    /**
     * Obtém o tipo de anexo baseado em uma extensão de arquivo.
     */
    public static MessageAttachmentType fromExtension(String extension) {
        for (MessageAttachmentType type : values()) {
            if (type.supportsExtension(extension)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Extensão não suportada: " + extension);
    }
}
