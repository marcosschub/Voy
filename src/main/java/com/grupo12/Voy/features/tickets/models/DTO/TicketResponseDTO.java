package com.grupo12.Voy.features.tickets.models.DTO;

import com.grupo12.Voy.features.parties.models.PartyEntity;
import com.grupo12.Voy.features.receipts.DTO.ReceiptResponseDTO;
import com.grupo12.Voy.features.receipts.models.ReceiptEntity;
import com.grupo12.Voy.features.users.models.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Builder
@Getter
@Setter
public class TicketResponseDTO {
    @NotNull
    private UUID idExternal;
    @NotNull
    private UUID userExternalId;
    @NotNull
    @Email
    private String userEmail;
    @NotNull
    private PartyResponseDTO party;
    @NotNull
    private Boolean confirmed;
    @NotNull
    private ReceiptResponseDTO receipt;
}
