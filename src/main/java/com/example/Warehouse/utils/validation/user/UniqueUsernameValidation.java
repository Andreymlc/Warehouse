package com.example.Warehouse.utils.validation.user;

import com.example.Warehouse.domain.repositories.contracts.user.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueUsernameValidation implements ConstraintValidator<UniqueUsername, String> {
    private final UserRepository userRepo;

    public UniqueUsernameValidation(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return userRepo.findByUsername(username).isEmpty();
    }
}
