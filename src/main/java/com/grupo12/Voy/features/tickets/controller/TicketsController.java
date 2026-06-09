package com.grupo12.Voy.features.tickets.controller;

import com.grupo12.Voy.features.tickets.models.DTO.TicketAndReceiptDto;
import com.grupo12.Voy.features.tickets.models.DTO.TicketRequestDTO;
import com.grupo12.Voy.features.tickets.models.DTO.TicketResponseDTO;
import com.grupo12.Voy.features.tickets.service.ITicketsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/voy/tickets")
public class TicketsController {
    private final ITicketsService ticketsService;

    @GetMapping
    public ResponseEntity<List<TicketResponseDTO>> getAll(@RequestParam(required = false) UUID externalId,
                                                          @RequestParam(required = false) Boolean isConfirmed,
                                                          @RequestParam(required = false) String title,
                                                          @RequestParam(required = false) String usernameOrganizer,
                                                          @RequestParam(required = false) String usernameUser
    ){
        return ResponseEntity.ok(ticketsService.getAll(externalId,isConfirmed,
                title,usernameOrganizer,usernameUser));
    }

    @PostMapping
    public ResponseEntity<TicketAndReceiptDto> purchaseTickets(@RequestBody @Valid TicketRequestDTO ticketDTO){
        return ResponseEntity.ok(ticketsService.purchaseTickets(ticketDTO));
    }

    @PatchMapping("/transferTicket/{userId}/{ticketId}")
    public  ResponseEntity<TicketResponseDTO> transferTicket(@PathVariable UUID userId,
                                                             @PathVariable UUID ticketId,
                                                             @RequestParam UUID newUserExtId){
        return ResponseEntity.ok(ticketsService.transferTicket(ticketId,userId,newUserExtId));
    }

    /// Solo el admin deberia poder entrar a este endpoint
    @PatchMapping("/confirmTickets/{receiptExtId}")
    public ResponseEntity<TicketAndReceiptDto> confirmPurchase(@PathVariable UUID receiptExtId){
        return ResponseEntity.ok(ticketsService.confirmPurchase(receiptExtId));
    }

    @DeleteMapping("/returnTicket/{userId}/{ticketId}")
    public ResponseEntity<Void> returnTicket(@PathVariable UUID userId,
                                             @PathVariable UUID ticketId){
        ticketsService.returnTicket(userId,ticketId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/rejectPurchase/{receiptId}")
    public ResponseEntity<Void> rejectPurchase(@PathVariable UUID receiptId){
        ticketsService.rejectPurchase(receiptId);
        return ResponseEntity.noContent().build();
    }
}
