package com.grupo12.Voy.features.parties.Dto;

import java.time.LocalDateTime;

public record PartyUsersDto(
        String title,
        String descripcion,
        String city,
        String adress,
        LocalDateTime dateTime,
        String organizer
) {
}
