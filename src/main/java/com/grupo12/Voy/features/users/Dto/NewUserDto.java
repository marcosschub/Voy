package com.grupo12.Voy.features.users.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record NewUserDto (
        @NotBlank(message = "El email no puede estar en blanco")
        @Email(regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$",
        message = "Email invalido")
        String email,
        @NotBlank(message = "El nombre de usuario no puede estar en blanco")
        String username,
        @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,20}$",
                message = "La contraseña debe tener entre 8 y 20 caracteres, incluyendo mayúsculas, minúsculas, números y caracteres especiales.")
        String password) {
}
