package com.grupo12.Voy.features.receipts.service;

import com.grupo12.Voy.features.receipts.DTO.ReceiptRequestDTO;
import com.grupo12.Voy.features.receipts.DTO.ReceiptResponseDTO;
import com.grupo12.Voy.features.tickets.models.DTO.TicketRequestDTO;
import com.grupo12.Voy.features.tickets.models.DTO.TicketResponseDTO;

import java.util.List;
import java.util.UUID;

public interface IReceiptService {
    List<ReceiptResponseDTO> getAll();
    ReceiptResponseDTO getByExternalId(UUID id);
    List<ReceiptResponseDTO> getByPaymentMethod(String method);
    ReceiptResponseDTO createReceipt(ReceiptRequestDTO dto);
    void deleteReceipt(UUID externalID,UUID userExtId);
    List<ReceiptResponseDTO> getByUser(UUID userExtId);
}
