package com.grupo12.Voy.features.tags.dto;

import jakarta.validation.constraints.NotBlank;

public record TagsDTO(@NotBlank String name){
}