package com.grupo12.Voy.features.parties.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
@NoArgsConstructor
@Data
@AllArgsConstructor

public class PartyResDTO {
    private UUID idExternal;
    private String title;
    private String description;
    private String city;
    private String adress;
    private LocalDateTime dateTime;
    private Integer guestLimit;
    private Boolean partyAccesibility;
    private Boolean state;
    private String organizerName; ///deberia mostrar username no el email, pero por ahora dejo el email como identificador
    private Set<String> tags;
}
