package com.example.Warehouse.utils.validation.warehouse;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy = UniqueWarehouseNameValidation.class)
public @interface UniqueWarehouseName {
    String message() default "Category already exists";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
