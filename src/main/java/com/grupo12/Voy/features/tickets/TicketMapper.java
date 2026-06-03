package com.grupo12.Voy.features.tickets;

import com.grupo12.Voy.features.parties.mapper.PartyMapper;
import com.grupo12.Voy.features.receipts.ReceiptMapper;
import com.grupo12.Voy.features.tickets.models.DTO.TicketRequestDTO;
import com.grupo12.Voy.features.tickets.models.DTO.TicketResponseDTO;
import com.grupo12.Voy.features.tickets.models.DTO.TicketUsersDto;
import com.grupo12.Voy.features.tickets.models.TicketEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "string", uses = {PartyMapper.class,ReceiptMapper.class})
public interface TicketMapper {
    @Mapping(target = "userIdExternal", source = "user.idExternal")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "receipt", source = "receiptEntity")
    @Mapping(target = "party", source = "partyEntity")
    TicketResponseDTO toResponseDto(TicketEntity ticket);

    @Mapping(target = "userName", source = "user.userName")
    @Mapping(target = "userEmail", source = "user.email")
    @Mapping(target = "receipt", source = "receiptEntity")
    @Mapping(target = "party", source = "partyEntity")
    TicketUsersDto toUsersDto(TicketEntity ticket);

    @Mapping(target = "idTicket", ignore = true)
    @Mapping(target = "idExternal", ignore = true)
    @Mapping(target = "confirmed", ignore = true)
    @Mapping(target = "user", ignore = true)
    @Mapping(target = "party", ignore = true)
    @Mapping(target = "receipt", ignore = true)
    TicketEntity toEntity(TicketRequestDTO ticket);

    TicketEntity toEntityFromMapper(TicketResponseDTO responseDTO);
}
