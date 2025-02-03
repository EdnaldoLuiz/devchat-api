package com.ednaldoluiz.websocket.infra.annotation.phone;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidator.class)
public @interface Phone {

    String message() default "Telefone inválido";
    Class<?>[] groups() default {};
    Class<?>[] payload() default {};
    
}
