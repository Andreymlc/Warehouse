package com.example.Warehouse.utils.validations.product;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ExistProductValidation.class)
public @interface ExistProduct {
    String message() default "Продукт не найден";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
