package com.grupo12.Voy.features.tags.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class TagsDTO{
    @NotBlank
    private String name;

    public TagsDTO(String name){
        this.name=name.toUpperCase();
    }
}