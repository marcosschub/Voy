package com.grupo12.Voy.features.receipts.service;

import com.grupo12.Voy.common.exceptions.ExceededAmountException;
import com.grupo12.Voy.common.exceptions.NotAllowedException;
import com.grupo12.Voy.features.parties.service.PartyService;
import com.grupo12.Voy.features.receipts.DTO.ReceiptResponseDTO;
import com.grupo12.Voy.features.receipts.DTO.ReceiptRequestDTO;
import com.grupo12.Voy.features.receipts.ReceiptRepository;
import com.grupo12.Voy.features.receipts.models.ReceiptEntity;
import com.grupo12.Voy.features.receipts.ReceiptMapper;
import com.grupo12.Voy.features.tickets.models.DTO.TicketRequestDTO;
import com.grupo12.Voy.features.tickets.models.DTO.TicketResponseDTO;
import com.grupo12.Voy.features.tickets.service.TicketsService;
import com.grupo12.Voy.features.users.Mapper.UserMapper;
import com.grupo12.Voy.features.users.Service.UsersService;
import com.grupo12.Voy.features.users.UserRepository;
import com.grupo12.Voy.features.users.models.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReceiptsService implements IReceiptService {

    private final ReceiptRepository receiptRepository;
    private final UsersService usersService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ReceiptMapper receiptMapper;
    private final TicketsService ticketsService;
    private final PartyService partyService;

    public List<ReceiptResponseDTO> getAll(){
        return receiptRepository.findAll().stream().map(receiptMapper::toResponseDTO).toList();
    }

    public ReceiptResponseDTO getById(Long id){
        return receiptMapper.toResponseDTO(receiptRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el recibo")));
    }

    public ReceiptResponseDTO getByExternalId(UUID id){
        ReceiptEntity receipt = receiptRepository
                .findByExternalId(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe un recibo con ese ID"));
        return receiptMapper.toResponseDTO(receipt);
    }

    public List<ReceiptResponseDTO> getByPaymentMethod(String method){
        return receiptRepository.findByPaymentMethod(method.toUpperCase())
                .stream().map(receiptMapper::toResponseDTO)
                .toList();
    }

    public List<ReceiptResponseDTO> getByUser(UUID userExtId){
        UserEntity user = userMapper.userToEntity(usersService.findByExternalId(userExtId));
        return receiptRepository.findByUser(user)
                .stream().map(receiptMapper::toResponseDTO).toList();
    }

    @Transactional
    public ReceiptResponseDTO createReceipt(ReceiptRequestDTO dto){
        UserEntity user = userMapper.userToEntity(userRepository.findByEmail(dto.user().email())
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el usuario")));
        ReceiptEntity receipt = receiptMapper.toEntity(dto);
        receipt.setUser(user);
        return receiptMapper.toResponseDTO(receipt);
    }

    @Transactional
    public void deleteReceipt(UUID externalID, UUID userExtId){
        ReceiptEntity receipt = receiptRepository.findByExternalId(externalID)
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el recibo"));
        if(userExtId != receipt.getUser().getIdExternal()){
            throw new NotAllowedException("Para eliminar el recibo debes ser el usuario que lo adquirio");
        }
        receiptRepository.delete(receipt);
    }

    @Transactional
    public List<TicketResponseDTO> purchaseTickets(ReceiptRequestDTO receiptDTO, TicketRequestDTO ticketDTO){
        int available = partyService.getByExternalId(ticketDTO.partyIdExternal()).guestLimit() - ticketsService.getByParty(ticketDTO.partyIdExternal()).size();
        if (receiptDTO.quantity() > available){
            throw new ExceededAmountException("Solo quedan "+ available + "entradas disponibles");
        }
        ReceiptResponseDTO receipt = createReceipt(receiptDTO);
        return ticketsService.createTicket(ticketDTO,receipt.quantity());
    }

    /*
    calcularDescuentoPorMedioDePago
    calcularPrecioFinal
     */
}
