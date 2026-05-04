package com.grupo12.Voy.features.tickets;

import com.grupo12.Voy.features.tickets.models.TicketEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity,Long> {
}
