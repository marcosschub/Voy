package com.grupo12.Voy.features.users.models;

import com.grupo12.Voy.features.roles.models.RoleEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name ="usuarios")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID idExternal;

    @NotNull
    private String email;

    @NotNull
    private String password;

    @ColumnDefault("false")
    private Boolean accesibility;

    @NotNull
    @Column(name = "fecha_nacimiento")
    private Date birthDate;

    @ManyToMany
    @JoinTable(name = "rol_por_usuario",
                joinColumns = @JoinColumn(name = "usuario_id"),
                inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<RoleEntity> role;

    @PrePersist
    public void onSave(){
        if(idExternal==null) {
            idExternal = UUID.randomUUID();
        }
    }
}
