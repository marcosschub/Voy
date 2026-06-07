package com.grupo12.Voy.features.tickets.service;

import com.grupo12.Voy.features.receipts.DTO.ReceiptRequestDTO;
import com.grupo12.Voy.features.receipts.models.ReceiptEntity;
import com.grupo12.Voy.features.tickets.models.DTO.TicketAndReceiptDto;
import com.grupo12.Voy.features.tickets.models.DTO.TicketRequestDTO;
import com.grupo12.Voy.features.tickets.models.DTO.TicketResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ITicketsService {
    TicketResponseDTO getByExternalId (UUID id);
    List<TicketResponseDTO> getByUser(UUID id);
    List<TicketResponseDTO> getByUserEmail(String email);
    List<TicketResponseDTO> getByParty(UUID partyId);
    List<TicketResponseDTO> getByUserAndParty(UUID userId,UUID partyId);
    List<TicketResponseDTO> getByPartyAndConfirmed(UUID partyId);
    List<TicketResponseDTO> getByPartyAndUnconfirmed(UUID partyIdExt);
    List<TicketResponseDTO> getByReceipt(UUID receiptExtId);
    TicketAndReceiptDto createTicket(TicketRequestDTO request, ReceiptEntity receipt);
    TicketResponseDTO transferTicket(UUID externalId, UUID oldUserId, UUID newUserExtID);
    TicketResponseDTO acceptTicket(UUID userExtId, UUID externalId);
    void returnTicket(UUID ticketExtId, UUID userExtId);
    TicketAndReceiptDto purchaseTickets(TicketRequestDTO ticketDTO);
}
