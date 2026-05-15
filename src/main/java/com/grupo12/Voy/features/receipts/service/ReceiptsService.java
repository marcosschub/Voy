package com.grupo12.Voy.features.receipts.service;

import com.grupo12.Voy.features.receipts.DTO.ReceiptResponseDTO;
import com.grupo12.Voy.features.receipts.DTO.ReceiptRequestDTO;
import com.grupo12.Voy.features.receipts.ReceiptRepository;
import com.grupo12.Voy.features.receipts.models.ReceiptEntity;
import com.grupo12.Voy.features.receipts.ReceiptMapper;
import com.grupo12.Voy.features.users.UserRepository;
import com.grupo12.Voy.features.users.models.UserEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ReceiptsService {

    private final ReceiptRepository receiptRepository;
    private final UserRepository userRepository;

    @Autowired
    private ReceiptMapper receiptMapper;

    public List<ReceiptResponseDTO> getAllDTO(){
        return receiptRepository.findAll().stream().map(receiptMapper::toResponseDTO).toList();
    }

    public ReceiptResponseDTO getByExternalId(UUID id){
        ReceiptEntity receipt = receiptRepository
                .findByExternalId(id)
                .orElseThrow(() -> new EntityNotFoundException("No existe un recibo con ese ID"));
        return receiptMapper.toResponseDTO(receipt);
    }

    public List<ReceiptResponseDTO> getByPaymentMethod(String method){
        return receiptRepository.findByPaymentMethod(method.toUpperCase())
                .stream()
                .map(receiptMapper::toResponseDTO)
                .toList();
    }

    public ReceiptResponseDTO createReceipt(ReceiptRequestDTO dto){
        UserEntity user = userRepository.findByEmail(dto.getUserEmail());
        ReceiptEntity receipt = receiptMapper.toEntity(dto);
        receipt.setUser(user);
        return receiptMapper.toResponseDTO(receipt);
    }

    public void deleteReceipt(UUID externalID){
        ReceiptEntity receipt = receiptRepository.findByExternalId(externalID)
                .orElseThrow(() -> new EntityNotFoundException("No se encuentra el recibo"));
        receiptRepository.delete(receipt);
    }

    /*
    calcularDescuentoPorMedioDePago
    calcularPrecioFinal
     */
}
