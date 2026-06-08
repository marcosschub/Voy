package com.grupo12.Voy.features.receipts.service;

import com.grupo12.Voy.features.receipts.DTO.ReceiptRequestDTO;
import com.grupo12.Voy.features.receipts.DTO.ReceiptResponseDTO;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IReceiptService {
    public List<ReceiptResponseDTO> getAll(
            UUID externalId, String paymentMethod,
            BigDecimal minPrice, BigDecimal maxPrice,
            BigDecimal minFinalPrice, BigDecimal maxFinalPrice,
            LocalDateTime from, LocalDateTime to,
            Integer minQuantity, Integer maxQuantity);
    ReceiptResponseDTO getByExternalId(UUID id);
    List<ReceiptResponseDTO> getByPaymentMethod(String method);
    ReceiptResponseDTO createReceipt(ReceiptRequestDTO dto);
    void deleteReceipt(UUID externalID,UUID userExtId);
    List<ReceiptResponseDTO> getByUser(UUID userExtId);
}
