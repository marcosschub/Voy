package com.grupo12.Voy.features.users.Dto;

import java.util.Objects;
import java.util.UUID;

public record UserDto(UUID userId, String userName, String email, String password){
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof UserDto userDto)) return false;
        return Objects.equals(userId, userDto.userId) && Objects.equals(email, userDto.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, email);
    }
}
