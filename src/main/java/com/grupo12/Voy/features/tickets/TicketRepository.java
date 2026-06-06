package com.grupo12.Voy.features.tickets;

import com.grupo12.Voy.features.parties.models.PartyEntity;
import com.grupo12.Voy.features.receipts.models.ReceiptEntity;
import com.grupo12.Voy.features.tickets.models.DTO.TicketResponseDTO;
import com.grupo12.Voy.features.tickets.models.TicketEntity;
import com.grupo12.Voy.features.users.models.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity,Long> {
    Optional<TicketEntity> findByIdExternal(UUID id);
    List<TicketEntity> findByUser(UserEntity user);
    List<TicketEntity> findByReceiptEntity(ReceiptEntity receipt);
    List<TicketEntity> findByParty(PartyEntity party);
    List<TicketEntity> findByUserAndParty(UserEntity user,PartyEntity party);
}
