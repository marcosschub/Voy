package com.grupo12.Voy.features.roles.models;

import com.grupo12.Voy.features.roles.Role;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "nombre_rol", nullable = false)
    private Role nombreRol;

}
