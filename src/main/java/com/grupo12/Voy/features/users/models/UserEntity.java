package com.grupo12.Voy.features.users.models;

import com.grupo12.Voy.features.parties.models.PartyEntity;
import com.grupo12.Voy.features.receipts.models.ReceiptEntity;
import com.grupo12.Voy.features.tickets.models.TicketEntity;
import com.grupo12.Voy.features.users.Role;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name ="usuarios")
@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id",nullable = false,unique = true)
    private UUID externalId;

    @Column(name = "usuario",nullable = false,unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(name = "contraseña",nullable = false)
    private String password;

    @Column(name = "accesibilidad",nullable = false)
    private Boolean accesibilityUser;

    @Column(name = "fecha_nacimiento",nullable = false)
    private Date birthdate;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "roles_usuario", joinColumns = @JoinColumn(name = "roles"))
    private List<Role> role;

    @ManyToMany
    @JoinTable(
            name = "usuarios_seguidos",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_seguido")
    )
    private List<UserEntity> followsList;

    @ManyToMany(mappedBy = "followsList")
    private List<UserEntity> followersList;

    @ManyToMany
    @JoinTable(
            name = "eventos_seguidos",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_evento")
    )
    private List<PartyEntity> followedParties;


    @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<PartyEntity> myParties;

    @OneToMany(fetch = FetchType.LAZY)
    private List<TicketEntity> myTickets;

    @OneToMany(fetch = FetchType.LAZY)
    private List<ReceiptEntity> myReceipts;

    @PrePersist
    public void onSave(){
        if(externalId==null) {
            externalId = UUID.randomUUID();
        }
        if(accesibilityUser==null) {
            accesibilityUser = false;
        }
    }
}
