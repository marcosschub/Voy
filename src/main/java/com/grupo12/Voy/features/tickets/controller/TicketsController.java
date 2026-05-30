package com.grupo12.Voy.features.tickets.controller;


import com.grupo12.Voy.features.tickets.models.DTO.TicketRequestDTO;
import com.grupo12.Voy.features.tickets.models.DTO.TicketResponseDTO;
import com.grupo12.Voy.features.tickets.service.ITicketsService;
import com.grupo12.Voy.features.tickets.service.TicketsService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
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
    public ResponseEntity<TicketResponseDTO> getByExternalId(@PathVariable @Valid UUID externalId){
        return ResponseEntity.ok(ticketsService.getByExternalId(externalId));
    }

    @GetMapping("/filterByUser/{userId}")
    public ResponseEntity<List<TicketResponseDTO>> getByUser(@PathVariable @Valid UUID userId){
        return ResponseEntity.ok(ticketsService.getByUser(userId));
    }

    @GetMapping("/filterByParty/{partyId}")
    public ResponseEntity<List<TicketResponseDTO>> getByParty(@PathVariable UUID partyId){
        return ResponseEntity.ok(ticketsService.getByParty(partyId));
    }

    @GetMapping("/{userId}/{partyId}")
    public ResponseEntity<List<TicketResponseDTO>> getByPartyAndUser(@PathVariable @Valid UUID userId,
                                                                     @PathVariable @Valid UUID partyId){
        return ResponseEntity.ok(ticketsService.getByUserAndParty(userId,partyId));
    }

    @GetMapping("/confirmedTicketsByParty")
    public ResponseEntity<List<TicketResponseDTO>> getConfirmedTicketsByParty(@RequestParam @Valid UUID partyId){
        return ResponseEntity.ok(ticketsService.getByPartyAndConfirmed(partyId));
    }

    @GetMapping("/unconfirmedTicketsByParty")
    public ResponseEntity<List<TicketResponseDTO>> getUnconfirmedTicketsByParty(@RequestParam @Valid UUID partyId){
        return ResponseEntity.ok(ticketsService.getByPartyAndUnconfirmed(partyId));
    }

    @PostMapping
    public ResponseEntity<List<TicketResponseDTO>> createTickets(@RequestBody @Valid TicketRequestDTO ticketReq,
                                                                 @RequestParam Integer cantidad){
        return ResponseEntity.ok(ticketsService.createTicket(ticketReq,cantidad));
    }

    @PatchMapping("/transferTicket")
    public  ResponseEntity<TicketResponseDTO> transferTicket(@RequestParam @Valid UUID ticketExtId,
                                                             @RequestParam @Valid UUID newUserExtId){
        return ResponseEntity.ok(ticketsService.transferTicket(ticketExtId,newUserExtId));
    }

    @PatchMapping
    public ResponseEntity<TicketResponseDTO> acceptTicket(@PathVariable @Valid UUID userID,
                                                          @RequestParam @Valid UUID ticketId){
        return ResponseEntity.ok(ticketsService.acceptTicket(userID, ticketId));
    }
}
