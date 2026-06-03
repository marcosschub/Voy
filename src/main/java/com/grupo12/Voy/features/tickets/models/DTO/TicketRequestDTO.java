package com.grupo12.Voy.features.tickets.models.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

public record TicketRequestDTO (
    @NotNull (message = "Ingrese el UUID del usuario")
    UUID userIdExternal,
    @NotNull (message = "Ingrese el UUID de la fiesta")
    UUID partyIdExternal,
    @NotNull (message = "Ingrese el UUID del recibo")
    UUID receiptExternalId){
}
