package com.example.Warehouse.utils.validations.category;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = ExistingCategoryIdValidation.class)
public @interface ExistingCategoryId {
    String message() default "Category not found";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
