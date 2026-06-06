package com.grupo12.Voy.features.parties.Dto;

import com.grupo12.Voy.features.tags.dto.TagsDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;
import java.util.List;

public record PartyReqDTO(
        @NotNull
        Long idOrganizer,
        @NotBlank(message = "El titulo es obligatorio")
        String title,
        @NotNull(message = "La descripcion no puede ser nula")
        String description,
        @NotBlank(message = "La ciudad es obligatoria")
        String city,
        @NotBlank(message = "La direccion es obligatoria")
        String adress,
        @NotNull(message = "La fecha es obligatoria")
        LocalDateTime dateTime,
        @NotNull(message = "El limite de invitados no puede ser nulo")
        @Positive(message = "El limite de invitados tiene que ser mayor a 0")
        Integer guestLimit,
        @NotNull(message = "La visibilidad del evento no puede ser nulo")
        Boolean partyAccesibility) {
}
