package com.grupo12.Voy.features.receipts.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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
    @NotNull
    private UUID externalId;
    @NotNull
    @Positive
    private BigDecimal price;
    @NotNull
    private String paymentMethod;
    @NotNull
    @Positive
    private BigDecimal finalPrice;
    @NotNull
    private LocalDateTime paymentDate;
    @NotNull
    @Positive
    private Integer quantity;
    @NotNull
    @Email
    private String userEmail;
}
