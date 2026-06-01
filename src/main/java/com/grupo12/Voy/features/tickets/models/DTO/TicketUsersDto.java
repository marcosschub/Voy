package com.grupo12.Voy.features.tickets.models.DTO;

import com.grupo12.Voy.features.parties.Dto.PartyResDTO;
import com.grupo12.Voy.features.receipts.DTO.ReceiptResponseDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TicketUsersDto {
    private String userName;
    private String userEmail;
    private PartyResDTO party;
    private Boolean confirmed;
    private ReceiptResponseDTO receipt;
}
