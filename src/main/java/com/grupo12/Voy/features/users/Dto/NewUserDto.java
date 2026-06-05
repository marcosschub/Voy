package com.grupo12.Voy.features.users.Dto;

import jakarta.validation.constraints.NotBlank;

public record NewUserDto (@NotBlank String email,
                          @NotBlank String password) {
}
