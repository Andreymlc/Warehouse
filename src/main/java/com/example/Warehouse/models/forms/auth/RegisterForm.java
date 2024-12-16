package com.example.Warehouse.models.forms.auth;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public final class RegisterForm {
    private String email;
    private boolean role;
    private String userName;
    private String password;
    private String confirmPassword;

    public @Email(message = "Некорректный E-mail") String getEmail() {
        return email;
    }

    public void setEmail(@Email(message = "Некорректный E-mail") String email) {
        this.email = email;
    }

    public boolean isRole() {
        return role;
    }

    public void setRole(boolean role) {
        this.role = role;
    }

    public @NotBlank(message = "Имя пользователя не может быть пустым") String getUserName() {
        return userName;
    }

    public void setUserName(@NotBlank(message = "Имя пользователя не может быть пустым") String userName) {
        this.userName = userName;
    }

    public @Size(min = 6, message = "Пароль должен быть длиной минимум 6 символов") String getPassword() {
        return password;
    }

    public void setPassword(@Size(min = 6, message = "Пароль должен быть длиной минимум 6 символов") String password) {
        this.password = password;
    }

    public @Size(min = 6, message = "Пароль должен быть длиной минимум 6 символов") String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(@Size(min = 6, message = "Пароль должен быть длиной минимум 6 символов") String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
