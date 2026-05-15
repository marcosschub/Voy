package com.grupo12.Voy.features.parties.models;

import com.grupo12.Voy.features.tags.models.TagsEntity;
import com.grupo12.Voy.features.tickets.models.TicketEntity;
import com.grupo12.Voy.features.users.models.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
@Getter
@Setter
@Entity
@Table(name = "eventos")
public class PartyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private Long idParty;

    private UUID idExternal;

    @ManyToOne
    @JoinColumn(name = "id_organizador", nullable = false)
    private UserEntity organizer;

    @Column(name = "titulo", nullable = false)
    private String tittle;

    @Column(name = "tipo")
    @ColumnDefault("False")
    private Boolean partyAccesibility; // publico/privado

    @Enumerated
    @ManyToMany
    @JoinTable(name = "etiquetas_por_evento",
               joinColumns = @JoinColumn(name = "evento_id"),
                inverseJoinColumns = @JoinColumn(name = "etiquetas_id"))
    private Set<TagsEntity> tagsSet;

    @Column(name = "localidad")
    private String city;

    @Column(name = "direccion")
    private String adress;

    @Column(name = "estado")
    @ColumnDefault("true")
    private Boolean logicState; // activo/borrado logico

    private Integer guestLimit;

    @OneToMany
    private Set<TicketEntity> ticketsParty;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String description;

    @ManyToMany(mappedBy = "partiesSet")
    private Set<UserEntity> usersSet;

    private LocalDateTime dateTime;

    private Boolean state;

    @PrePersist
    public void onSave(){
        if(state==null){
            state=true;
        }
    }
}
