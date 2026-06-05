package com.grupo12.Voy.features.users.Dto;

import jakarta.validation.constraints.NotBlank;

public record UserUpdateDto(String userName,
                             String password) {
}
