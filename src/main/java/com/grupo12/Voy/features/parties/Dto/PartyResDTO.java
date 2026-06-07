package com.grupo12.Voy.features.parties.Dto;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record PartyResDTO (
     UUID externalId,
     String title,
     String description,
     String city,
     String adress,
     LocalDateTime dateTime,
     Integer guestLimit,
     Boolean partyAccesibility,
     Boolean state,
     String organizerName, ///deberia mostrar username no el email, pero por ahora dejo el email como identificador
     Set<String> tags)
    {}
