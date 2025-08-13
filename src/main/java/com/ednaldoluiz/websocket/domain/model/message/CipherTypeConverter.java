package com.ednaldoluiz.websocket.domain.model.message;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class CipherTypeConverter implements AttributeConverter<CipherType, Short> {

    @Override
    public Short convertToDatabaseColumn(CipherType attr) {
        return attr.getCode();
    }

    @Override
    public CipherType convertToEntityAttribute(Short db) {
        return CipherType.from(db);
    }
}
