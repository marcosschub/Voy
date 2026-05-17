package com.grupo12.Voy.features.tickets.service;

import com.grupo12.Voy.common.exceptions.EntityNotFoundException;
import com.grupo12.Voy.common.exceptions.ExceededAmountException;
import com.grupo12.Voy.features.parties.PartyRepository;
import com.grupo12.Voy.features.parties.models.PartyEntity;
import com.grupo12.Voy.features.receipts.ReceiptRepository;
import com.grupo12.Voy.features.tickets.TicketMapper;
import com.grupo12.Voy.features.tickets.TicketRepository;
import com.grupo12.Voy.features.receipts.models.ReceiptEntity;
import com.grupo12.Voy.features.tickets.models.DTO.TicketRequestDTO;
import com.grupo12.Voy.features.tickets.models.DTO.TicketResponseDTO;
import com.grupo12.Voy.features.tickets.models.TicketEntity;
import com.grupo12.Voy.features.users.UserRepository;
import com.grupo12.Voy.features.users.models.UserEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TicketsSevice {
    private final TicketRepository ticketRepository;
    @Autowired
    private TicketMapper ticketMapper;
    private final UserRepository userRepository;
    private final PartyRepository partyRepository;
    private final ReceiptRepository receiptRepository;

    public List<TicketResponseDTO> getAll(){
        return ticketRepository.findAll()
                .stream().map(ticketMapper::toResponseDto).toList();
    }

    public TicketResponseDTO getById(Long id){
        return ticketRepository.findById(id)
                .map(ticketMapper::toResponseDto)
                .orElseThrow(()->new EntityNotFoundException("Ticket no encontrado"));
    }

    public TicketResponseDTO getByExternalId (UUID id){
        return ticketRepository.findByIdExternal(id)
                .map(ticketMapper::toResponseDto)
                .orElseThrow(() -> new EntityNotFoundException("Ticket no encontrado"));
    }

    public List<TicketResponseDTO> getByUserIDExternal(UUID userId){
        UserEntity user = userRepository.getByIdExternal(userId);
        return ticketRepository.findByUser(user)
                .stream()
                .map(ticketMapper::toResponseDto)
                .toList();
    }

    public List<TicketResponseDTO> getByUserEmail(String email){
        UserEntity user = userRepository.getByEmail(email);
        return ticketRepository.findByUser(user)
                .stream()
                .map(ticketMapper::toResponseDto)
                .toList();
    }

    public List<TicketResponseDTO> getByParty(UUID partyId){
        PartyEntity party = partyRepository.getByIdExternal(partyId);
        return ticketRepository.findByParty(party)
                .stream()
                .map(ticketMapper::toResponseDto)
                .toList();
    }

    public List<TicketResponseDTO> getByUserAndParty(UUID userId,UUID partyId){
        UserEntity user = userRepository.getByIdExternal(userId);
        PartyEntity party = partyRepository.getByIdExternal(partyId);
        return ticketRepository.findByUserAndParty(user,party)
                .stream()
                .map(ticketMapper::toResponseDto)
                .toList();
    }

    public List<TicketResponseDTO> getByPartyAndConfirmed(UUID partyId){
        PartyEntity party = partyRepository.getByIdExternal(partyId);
        return ticketRepository.findByParty(party).stream()
                .filter(x -> x.getConfirmed() == true)
                .map(ticketMapper::toResponseDto)
                .toList();
    }

    public List<TicketResponseDTO> getByPartyAndUnconfirmed(UUID partyIdExt){
        PartyEntity party = partyRepository.getByIdExternal(partyIdExt);
        return ticketRepository.findByParty(party).stream()
                .filter(x -> x.getConfirmed() == false)
                .map(ticketMapper::toResponseDto)
                .toList();
    }

    public List<TicketResponseDTO> getByReceipt(UUID receiptExtId){
        ReceiptEntity receipt = receiptRepository.findByExternalId(receiptExtId)
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el recibo"));
        return ticketRepository.findByReceipt(receipt)
                .stream()
                .map(ticketMapper::toResponseDto)
                .toList();
    }

    public List<TicketResponseDTO> createTicket(TicketRequestDTO request, int quantity){
        UserEntity user = userRepository
                .findByIdExternal(request.getUserIdExternal())
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el usuario solicitado"));
        PartyEntity party = partyRepository
                .findByIdExternal(request.getPartyIdExternal())
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra la fiesta solicitada"));
        ReceiptEntity receipt = receiptRepository
                .findByExternalId(request.getReceiptExternalId())
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el recibo"));
        int available = party.getGuestLimit() - ticketRepository.findByParty(party).size();
        if (quantity > available){
            throw new ExceededAmountException("Solo quedan "+ available + " disponibles");
        }
        else{
            for(int i = 0; i < quantity; i++){
                TicketEntity ticket = new TicketEntity(user,party,receipt);
                ticketRepository.save(ticket);
            }
        }
        return ticketRepository.findByReceipt(receipt)
                .stream()
                .map(ticketMapper::toResponseDto)
                .toList();
    }

    @Transactional
    public TicketResponseDTO transferTicket(UUID externalId, UUID newUserExtID){
        TicketEntity ticket = ticketRepository.findByIdExternal(externalId)
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el ticket"));
        UserEntity user = userRepository.findByIdExternal(newUserExtID)
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el usuario solicitado"));
        ticket.setUser(user);
        TicketEntity ticket1 = ticketRepository.save(ticket);
        return ticketMapper.toResponseDto(ticket1);
    }

    public TicketResponseDTO acceptTicket(UUID externalId){
        TicketEntity ticket = ticketRepository.findByIdExternal(externalId)
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el ticket"));
        ticket.setConfirmed(true);
        TicketEntity ticket1 =  ticketRepository.save(ticket);
        return ticketMapper.toResponseDto(ticket1);
    }

    public void returnTicket(UUID externalId){
        TicketEntity ticket = ticketRepository.findByIdExternal(externalId)
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el Ticket"));
        ticketRepository.delete(ticket);
    }
}
