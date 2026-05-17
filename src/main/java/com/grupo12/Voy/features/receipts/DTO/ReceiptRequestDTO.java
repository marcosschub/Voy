package com.grupo12.Voy.features.receipts.DTO;

import com.grupo12.Voy.features.users.models.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder
@Getter
@Setter

public class ReceiptRequestDTO {
    @NotNull
    @Positive
    private BigDecimal price;
    @NotNull
    private String paymentMethod;
    @NotNull
    @Positive
    private Integer quantity;
    @NotNull
    private UserDto user;
}
