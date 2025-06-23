package com.ednaldoluiz.websocket.app.v1.signal.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

@Slf4j
public class ByteArraySizeValidator implements ConstraintValidator<ByteArraySize, byte[]> {

    private int expectedSize;

    @Override
    public void initialize(ByteArraySize annotation) {
        this.expectedSize = annotation.value();
    }

    @Override
    public boolean isValid(byte[] value, ConstraintValidatorContext context) {
        if (value == null) return true;
        if (value.length != expectedSize) {
            log.warn(
                    "The size of a byte array {} does not match the expected size {}",
                    Base64.getEncoder().encodeToString(value), expectedSize
            );
        }
        return value.length == expectedSize;
    }
}
