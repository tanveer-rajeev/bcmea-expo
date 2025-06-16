package com.betafore.evoting.Exception;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UniqueEmailValidator.class)
public @interface UniqueEmail {
    String message() default "Email already in use. Try another one";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
