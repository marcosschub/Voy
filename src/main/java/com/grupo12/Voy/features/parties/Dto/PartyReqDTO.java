package com.grupo12.Voy.features.parties.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;

public record PartyReqDTO(
        @NotNull
        Long idOrganizer,
        @NotBlank(message = "El titulo es obligatorio")
        String title,
        String description,
        @NotBlank(message = "La ciudad es obligatoria")
        String city,
        @NotBlank(message = "La direccion es obligatoria")
        String adress,
        @NotNull(message = "La fecha es obligatoria")
        LocalDateTime dateTime,
        Integer guestLimit,
        Boolean partyAccesibility,
        Set<Long> tagsIds) {
}
