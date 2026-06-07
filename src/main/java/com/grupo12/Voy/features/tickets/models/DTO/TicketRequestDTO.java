package com.grupo12.Voy.features.tickets.models.DTO;

import com.grupo12.Voy.features.users.Dto.UserDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

public record TicketRequestDTO (
        @NotNull
        @PositiveOrZero(message = "El monto debe ser 0 o positivo")
        BigDecimal price,
        @NotBlank(message = "Ingrese el metodo de pago")
        String paymentMethod,
        @NotNull
        @Positive(message = "La cantidad debe ser mayor a 0")
        Integer quantity,
        @NotNull (message = "Ingrese el UUID del usuario")
        UUID userIdExternal,
        @NotNull (message = "Ingrese el UUID de la fiesta")
        UUID partyIdExternal){
}
