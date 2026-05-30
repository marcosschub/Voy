package com.grupo12.Voy.features.tickets.service;

import com.grupo12.Voy.common.exceptions.EntityNotFoundException;
import com.grupo12.Voy.common.exceptions.ExceededAmountException;
import com.grupo12.Voy.common.exceptions.NotAllowed;
import com.grupo12.Voy.features.parties.PartyRepository;
import com.grupo12.Voy.features.parties.models.PartyEntity;
import com.grupo12.Voy.features.receipts.DTO.ReceiptResponseDTO;
import com.grupo12.Voy.features.receipts.ReceiptMapper;
import com.grupo12.Voy.features.receipts.ReceiptRepository;
import com.grupo12.Voy.features.tickets.TicketMapper;
import com.grupo12.Voy.features.tickets.TicketRepository;
import com.grupo12.Voy.features.receipts.models.ReceiptEntity;
import com.grupo12.Voy.features.tickets.models.DTO.TicketRequestDTO;
import com.grupo12.Voy.features.tickets.models.DTO.TicketResponseDTO;
import com.grupo12.Voy.features.tickets.models.TicketEntity;
import com.grupo12.Voy.features.users.Dto.UserDto;
import com.grupo12.Voy.features.users.Mapper.UserMapper;
import com.grupo12.Voy.features.users.Service.UsersService;
import com.grupo12.Voy.features.users.UserRepository;
import com.grupo12.Voy.features.users.models.UserEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TicketsService  implements ITicketsService{
    private final TicketRepository ticketRepository;
    private TicketMapper ticketMapper;
    private final UsersService usersService;
    private final UserRepository userRepository;
    private final PartyRepository partyRepository;
    private final ReceiptRepository receiptRepository;
    private final UserMapper userMapper;

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
        return ticketMapper.toResponseDto(ticketRepository.findByIdExternal(id)
                .orElseThrow(() -> new EntityNotFoundException("Ticket no encontrado")));
    }

    public List<TicketResponseDTO> getByUser(UUID id){
        UserEntity user = userMapper.userToEntity(usersService.findByExternalId(id));
        return ticketRepository.findByUser(user).stream()
                .map(ticketMapper::toResponseDto).toList();
    }

    public List<TicketResponseDTO> getByUserEmail(String email){
        UserEntity user = userMapper.userToEntity(userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el usuario")));
        return ticketRepository.findByUser(user).stream()
                .map(ticketMapper::toResponseDto).toList();
    }

    public List<TicketResponseDTO> getByParty(UUID partyId){
        PartyEntity party = partyRepository.findByExternalId(partyId)
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el usuario"));
        return ticketRepository.findByParty(party).stream()
                .map(ticketMapper::toResponseDto).toList();
    }

    public List<TicketResponseDTO> getByUserAndParty(UUID userId,UUID partyId){
        UserEntity user = userMapper.userToEntity(userRepository.findByExternalId(userId)
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el usuario")));
        PartyEntity party = partyRepository.findByExternalId(partyId)
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra la fiesta"));
        return ticketRepository.findByUserAndParty(user,party).stream()
                .map(ticketMapper::toResponseDto).toList();
    }

    public List<TicketResponseDTO> getByPartyAndConfirmed(UUID partyId){
        PartyEntity party = partyRepository.findByExternalId(partyId)
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el evento"));
        return ticketRepository.findByParty(party).stream()
                .filter(x -> x.getConfirmed() == true)
                .map(ticketMapper::toResponseDto)
                .toList();
    }

    public List<TicketResponseDTO> getByPartyAndUnconfirmed(UUID partyIdExt){
        PartyEntity party = partyRepository.findByExternalId(partyIdExt)
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el evento"));
        return ticketRepository.findByParty(party).stream()
                .filter(x -> x.getConfirmed() == false)
                .map(ticketMapper::toResponseDto)
                .toList();
    }

    public List<TicketResponseDTO> getByReceipt(UUID receiptExtId){
        ReceiptEntity receipt = receiptRepository.findByExternalId(receiptExtId)
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el recibo"));
        return ticketRepository.findByReceipt(receipt).stream()
                .map(ticketMapper::toResponseDto).toList();
    }

    @Transactional
    public List<TicketResponseDTO> createTicket(TicketRequestDTO request, Integer quantity){
        UserEntity user = userMapper.userToEntity(userRepository
                .findByExternalId(request.getUserIdExternal())
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el usuario solicitado")));
        PartyEntity party = partyRepository
                .findByExternalId(request.getPartyIdExternal())
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
        return ticketRepository.findByReceipt(receipt).stream()
                .map(ticketMapper::toResponseDto).toList();
    }

    @Transactional
    public TicketResponseDTO transferTicket(UUID externalId, UUID newUserExtID){
        TicketEntity ticket = ticketRepository.findByIdExternal(externalId)
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el ticket"));
        UserEntity user = userMapper.userToEntity(userRepository.findByExternalId(newUserExtID)
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el usuario solicitado")));
        ticket.setUser(user);
        TicketEntity ticket1 = ticketRepository.save(ticket);
        return ticketMapper.toResponseDto(ticket1);
    }

    @Transactional
    public TicketResponseDTO acceptTicket(UUID userExtId, UUID externalId){
        TicketEntity ticket = ticketRepository.findByIdExternal(externalId)
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el ticket"));
        if (ticket.getUser().getIdExternal() != userExtId){
            throw new NotAllowed("Solo el usuario propietario del ticket lo puede aceptar");
        }
        ticket.setConfirmed(true);
        TicketEntity ticket1 =  ticketRepository.save(ticket);
        return ticketMapper.toResponseDto(ticket1);
    }

    public void returnTicket(UUID ticketExtId, UUID userId){
        TicketEntity ticket = ticketRepository.findByIdExternal(ticketExtId)
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el Ticket"));
        if (ticket.getUser().getIdExternal() != userId){
            throw new NotAllowed("Para devolver un ticket debe ser el propietario del mismo");
        }
        ticketRepository.delete(ticket);
    }
}
