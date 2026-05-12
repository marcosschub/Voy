package com.grupo12.Voy.features.tags.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "etiquetas")
@NoArgsConstructor
@Getter
@Setter
public class TagsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "etiquetas_id")
    private Long tagsId;

    @Column(name = "nombre", unique = true)
    private String name;
}
