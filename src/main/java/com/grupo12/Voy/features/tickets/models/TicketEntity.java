package com.grupo12.Voy.features.tickets.models;

import com.grupo12.Voy.features.parties.models.PartyEntity;
import com.grupo12.Voy.features.receipts.models.ReceiptEntity;
import com.grupo12.Voy.features.users.models.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "entradas")
@Getter
@Setter
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_entrada")
    private Long idTicket;

    private UUID idExternal;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "id_evento", nullable = false)
    private PartyEntity party;

    private Boolean confirmed;

    @ManyToOne
    @JoinColumn(name = "id_factura", nullable = false)
    private ReceiptEntity receiptEntity;

    public TicketEntity(UserEntity user, PartyEntity party, ReceiptEntity receiptEntity) {
        this.idExternal = UUID.randomUUID();
        this.user = user;
        this.party = party;
        this.confirmed = false;
        this.receiptEntity = receiptEntity;
    }
}
