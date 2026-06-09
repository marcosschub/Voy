package com.grupo12.Voy.features.tickets.service;

import com.grupo12.Voy.features.receipts.DTO.ReceiptRequestDTO;
import com.grupo12.Voy.features.receipts.models.ReceiptEntity;
import com.grupo12.Voy.features.tickets.models.DTO.TicketAndReceiptDto;
import com.grupo12.Voy.features.tickets.models.DTO.TicketRequestDTO;
import com.grupo12.Voy.features.tickets.models.DTO.TicketResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ITicketsService {
    List<TicketResponseDTO> getAll(UUID ticketId, Boolean isConfimed,
                                   String title, String usernameOrganizer, String usernameUser);
    List<TicketResponseDTO> getByParty(UUID partyId);
    TicketAndReceiptDto createTicket(TicketRequestDTO request, ReceiptEntity receipt);
    TicketResponseDTO transferTicket(UUID externalId, UUID oldUserId, UUID newUserExtID);
    TicketAndReceiptDto confirmPurchase(UUID receiptExtId);
    void rejectPurchase(UUID receiptExtId);
    void returnTicket(UUID ticketExtId, UUID userExtId);
    TicketAndReceiptDto purchaseTickets(TicketRequestDTO ticketDTO);
}
