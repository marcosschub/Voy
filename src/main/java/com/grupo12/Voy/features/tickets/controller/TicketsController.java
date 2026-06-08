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

    @GetMapping("/{externalId}")
    public ResponseEntity<TicketResponseDTO> getByExternalId(@PathVariable UUID externalId){
        return ResponseEntity.ok(ticketsService.getByExternalId(externalId));
    }

    @GetMapping("/filterByUser/{userId}")
    public ResponseEntity<List<TicketResponseDTO>> getByUser(@PathVariable UUID userId){
        return ResponseEntity.ok(ticketsService.getByUser(userId));
    }

    @GetMapping("/filterByParty/{partyId}")
    public ResponseEntity<List<TicketResponseDTO>> getByParty(@PathVariable UUID partyId){
        return ResponseEntity.ok(ticketsService.getByParty(partyId));
    }

    @GetMapping("/{userId}/{partyId}")
    public ResponseEntity<List<TicketResponseDTO>> getByPartyAndUser(@PathVariable UUID userId,
                                                                     @PathVariable UUID partyId){
        return ResponseEntity.ok(ticketsService.getByUserAndParty(userId,partyId));
    }

    @GetMapping("/confirmedTicketsByParty")
    public ResponseEntity<List<TicketResponseDTO>> getConfirmedTicketsByParty(@RequestParam UUID partyId){
        return ResponseEntity.ok(ticketsService.getByPartyAndConfirmed(partyId));
    }

    @GetMapping("/unconfirmedTicketsByParty")
    public ResponseEntity<List<TicketResponseDTO>> getUnconfirmedTicketsByParty(@RequestParam UUID partyId){
        return ResponseEntity.ok(ticketsService.getByPartyAndUnconfirmed(partyId));
    }

    @PostMapping
    public ResponseEntity<TicketAndReceiptDto> purchaseTickets(@RequestBody @Valid TicketRequestDTO ticketDTO){
        return ResponseEntity.ok(ticketsService.purchaseTickets(ticketDTO));
    }

    @PatchMapping("/{userId}/{ticketId}/transferTicket")
    public  ResponseEntity<TicketResponseDTO> transferTicket(@PathVariable UUID userId,
                                                             @PathVariable UUID ticketId,
                                                             @RequestParam UUID newUserExtId){
        return ResponseEntity.ok(ticketsService.transferTicket(ticketId,userId,newUserExtId));
    }

    @PatchMapping("/accept/{userId}")
    public ResponseEntity<TicketResponseDTO> acceptTicket(@PathVariable UUID userId,
                                                          @RequestParam UUID ticketId){
        return ResponseEntity.ok(ticketsService.acceptTicket(userId, ticketId));
    }
}
