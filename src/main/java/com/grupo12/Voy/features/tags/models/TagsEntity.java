package com.grupo12.Voy.features.tags.models;

import jakarta.persistence.*;

@Entity
@Table(name = "etiquetas")
public class TagsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "etiquetas_id")
    private Long tags_id;

    @Column(name = "nombre")
    private String name;
}
