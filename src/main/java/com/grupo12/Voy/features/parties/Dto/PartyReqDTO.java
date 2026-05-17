package com.grupo12.Voy.features.parties.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PartyReqDTO {
    @NotNull
    private Long idOrganizer;
    @NotBlank(message = "El titulo es obligatorio")
    private String title;
    private String description;
    @NotBlank(message = "La ciudad es obligatoria")
    private String city;
    @NotBlank(message = "La direccion es obligatoria")
    private String adress;
    @NotNull(message = "La fecha es obligatoria")
    private LocalDateTime dateTime;
    private Integer guestLimit;
    private Boolean partyAccesibility;
    private Set<Long> tagsIds;
}
