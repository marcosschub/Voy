package com.grupo12.Voy.features.tickets.controller;

import com.grupo12.Voy.features.tickets.models.DTO.TicketAndReceiptDto;
import com.grupo12.Voy.features.tickets.models.DTO.TicketPrivateRequestDTO;
import com.grupo12.Voy.features.tickets.models.DTO.TicketRequestDTO;
import com.grupo12.Voy.features.tickets.models.DTO.TicketResponseDTO;
import com.grupo12.Voy.features.tickets.service.ITicketsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@RestController
@RequestMapping("/api/tickets")
public class TicketsController {
    private final ITicketsService ticketsService;

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<TicketResponseDTO>> getAllAdmin(@RequestParam(required = false) UUID externalId,
                                                          @RequestParam(required = false) Boolean isConfirmed,
                                                          @RequestParam(required = false) String title,
                                                          @RequestParam(required = false) String usernameOrganizer,
                                                          @RequestParam(required = false) String usernameUser
    ){
        return ResponseEntity.ok(ticketsService.getAllAdmin(externalId,isConfirmed,
                title,usernameOrganizer,usernameUser));
    }

    @GetMapping("/organizer")
    @PreAuthorize("hasRole('ORGANIZATOR')")
    public ResponseEntity<List<TicketResponseDTO>> getAllOrganizer(@RequestParam(required = false) UUID externalId,
                                                          @RequestParam(required = false) Boolean isConfirmed,
                                                          @RequestParam(required = false) String title,
                                                          @AuthenticationPrincipal(expression = "usuario.externalId")UUID userExtId,
                                                          @RequestParam(required = false) String usernameUser
    ){
        return ResponseEntity.ok(ticketsService.getAllOrganizer(externalId,isConfirmed,
                title,userExtId,usernameUser));
    }

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<TicketResponseDTO>> getAll(@RequestParam(required = false) UUID externalId,
                                                          @RequestParam(required = false) Boolean isConfirmed,
                                                          @RequestParam(required = false) String title,
                                                          @RequestParam(required = false) String usernameOrganizer,
                                                          @AuthenticationPrincipal(expression = "usuario.externalId")UUID userExtId
    ){
        return ResponseEntity.ok(ticketsService.getAllUser(externalId,isConfirmed,
                title,usernameOrganizer,userExtId));
    }

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TicketAndReceiptDto> purchaseTicketsPublic(@AuthenticationPrincipal(expression = "usuario.externalId")UUID userExtId,
            @RequestBody @Valid TicketRequestDTO ticketDTO){
        return ResponseEntity.ok(ticketsService.purchaseTickets(userExtId,ticketDTO));
    }

    @PostMapping("/private")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TicketAndReceiptDto> getTicketsPrivate(@AuthenticationPrincipal(expression = "usuario.externalId")UUID userExtId,
                                                                 @RequestBody @Valid TicketPrivateRequestDTO ticketDTO){
        return ResponseEntity.ok(ticketsService.getTicketsPrivate(userExtId,ticketDTO));
    }

    @PatchMapping("/transferTicket/{ticketId}")
    @PreAuthorize("hasRole('USER')")
    public  ResponseEntity<TicketResponseDTO> transferTicket(@AuthenticationPrincipal(expression = "usuario.externalId")UUID userExtId,
                                                             @PathVariable UUID ticketId,
                                                             @RequestParam UUID newUserExtId){
        return ResponseEntity.ok(ticketsService.transferTicket(ticketId,userExtId,newUserExtId));
    }

    @PatchMapping("/confirmTickets/{receiptExtId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TicketAndReceiptDto> confirmPurchase(@PathVariable UUID receiptExtId){
        return ResponseEntity.ok(ticketsService.confirmPurchase(receiptExtId));
    }

    @DeleteMapping("/returnTicket/{ticketId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> returnTicket(@AuthenticationPrincipal(expression = "usuario.externalId")UUID userExtId,
                                             @PathVariable UUID ticketId){
        ticketsService.returnTicket(userExtId,ticketId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/rejectPurchase/{receiptId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> rejectPurchase(@PathVariable UUID receiptId){
        ticketsService.rejectPurchase(receiptId);
        return ResponseEntity.noContent().build();
    }
}
