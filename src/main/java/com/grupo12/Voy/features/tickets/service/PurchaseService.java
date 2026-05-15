package com.grupo12.Voy.features.tickets.service;

import com.grupo12.Voy.features.receipts.DTO.ReceiptRequestDTO;
import com.grupo12.Voy.features.receipts.DTO.ReceiptResponseDTO;
import com.grupo12.Voy.features.receipts.service.ReceiptsService;
import com.grupo12.Voy.features.tickets.models.DTO.TicketRequestDTO;
import com.grupo12.Voy.features.tickets.models.DTO.TicketResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PurchaseService {
    private final ReceiptsService receiptsService;
    private final TicketsSevice ticketsSevice;

    public List<TicketResponseDTO> purchaseTickets(ReceiptRequestDTO receiptDTO, TicketRequestDTO ticketDTO){
        ReceiptResponseDTO receipt = receiptsService.createReceipt(receiptDTO);
        return ticketsSevice.createTicket(ticketDTO,receipt.getQuantity());
    }

}
