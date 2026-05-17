package com.grupo12.Voy.features.receipts.DTO;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Getter
@Setter

public class ReceiptResponseDTO {
    private UUID externalId;
    private BigDecimal price;
    private String paymentMethod;
    private BigDecimal finalPrice;
    private LocalDateTime paymentDate;
    private Integer quantity;
    private UserDTO user;
}
