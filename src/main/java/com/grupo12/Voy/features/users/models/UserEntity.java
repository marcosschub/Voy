package com.grupo12.Voy.features.users.models;

import com.grupo12.Voy.features.parties.models.PartyEntity;
import com.grupo12.Voy.features.tickets.models.TicketEntity;
import com.grupo12.Voy.features.users.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.List;
import java.util.Set;
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

    private UUID idExternal;

    @NotNull
    private String userName;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @ColumnDefault("false")
    private Boolean accesibilityUser;

    @NotNull
    @Column(name = "fecha_nacimiento")
    private Date birthDate;

    @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "roles_usuario", joinColumns = @JoinColumn(name = "roles"))
    private List<Role> role;

    @ManyToMany
    @JoinTable(
            name = "usuarios_seguidos",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_seguido")
    )
    private Set<UserEntity> followsList;

    @ManyToMany(mappedBy = "followsSet")
    private List<UserEntity> followersList;

    @ManyToMany
    @JoinTable(
            name = "eventos_seguidos",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_evento")
    )
    private List<PartyEntity> followedParties;


    @ManyToMany
    @JoinTable(
            name = "eventos_creados",
            joinColumns = @JoinColumn(name = "id_usuario"),
            inverseJoinColumns = @JoinColumn(name = "id_eventos")
    )
    private List<PartyEntity> myParties;

    @OneToMany
    private List<TicketEntity> myTickets;

    @PrePersist
    public void onSave(){
        if(idExternal==null) {
            idExternal = UUID.randomUUID();
        }
    }
}
