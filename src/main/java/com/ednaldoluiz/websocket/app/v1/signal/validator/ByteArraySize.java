package com.ednaldoluiz.websocket.app.v1.signal.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ByteArraySizeValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ByteArraySize {
    String message() default "O array de bytes não possui o tamanho esperado";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
    int value();
}

