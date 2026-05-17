package com.grupo12.Voy.features.tickets;

import com.grupo12.Voy.features.tickets.models.DTO.TicketRequestDTO;
import com.grupo12.Voy.features.tickets.models.DTO.TicketResponseDTO;
import com.grupo12.Voy.features.tickets.models.TicketEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "string")
public interface TicketMapper {
    @Mapping(target = "userIdExternal", source = "user.idExternal")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "receipt", source = "receiptEntity")
    @Mapping(target = "party", source = "partyEntity")
    TicketResponseDTO toResponseDto(TicketEntity ticket);

    @Mapping(target = "idTicket", ignore = true)
    @Mapping(target = "idExternal", ignore = true)
    @Mapping(target = "confirmed", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "party", ignore = true)
    @Mapping(target = "receipt", ignore = true)
    TicketEntity toEntity(TicketRequestDTO ticket);
}
