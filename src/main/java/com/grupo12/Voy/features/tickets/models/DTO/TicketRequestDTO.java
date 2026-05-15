package com.grupo12.Voy.features.tickets.models.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Builder
@Getter
@Setter
public class TicketRequestDTO {
    @NotNull
    private UUID userIdExternal;
    @NotNull
    private UUID partyIdExternal;
    @NotNull
    private UUID receiptExternalId;
}
