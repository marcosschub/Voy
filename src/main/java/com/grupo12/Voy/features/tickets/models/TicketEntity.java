package com.grupo12.Voy.features.tickets.models;

import com.grupo12.Voy.features.parties.models.PartyEntity;
import com.grupo12.Voy.features.users.models.UserEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "entradas")
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrada")
    private Long idTicket;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "id_evento", nullable = false)
    private PartyEntity party;

    @Embedded
    private TicketDescriptionEntity ticketDescriptionEntity;
}
