package com.grupo12.Voy.features.users.Dto;

import java.util.UUID;


//Equal y hash sobreescribir //TODO
public record UserDto(UUID userId, String userName, String userEmail, String password){
}
