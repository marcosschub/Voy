package com.grupo12.Voy.features.parties.models;

import com.grupo12.Voy.features.tags.Tags;
import com.grupo12.Voy.features.tags.models.TagsEntity;
import com.grupo12.Voy.features.users.models.UserEntity;
import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "eventos")
public class PartyEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_evento")
    private Long idEvento;

    private UUID idExternal;

    @ManyToOne
    @JoinColumn(name = "id_organizador", nullable = false)
    private UserEntity organizador;

    @Column(name = "titulo", nullable = false)
    private String titulo;

    @Column(name = "tipo")
    @ColumnDefault("False")
    private Boolean tipo; // publico/privado

    @Enumerated
    @ManyToMany
    @JoinTable(name = "etiquetas_por_evento",
               joinColumns = @JoinColumn(name = "evento_id"),
                inverseJoinColumns = @JoinColumn(name = "etiquetas_id"))
    private Set<TagsEntity> listaTags;

    @Column(name = "localidad")
    private String localidad;

    @Column(name = "estado")
    @ColumnDefault("true")
    private Boolean estado; // activo/borrado logico

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
}
