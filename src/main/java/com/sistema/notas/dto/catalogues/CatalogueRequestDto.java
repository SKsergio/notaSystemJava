package com.sistema.notas.dto.catalogues;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CatalogueRequestDto(
        @NotBlank(message = "El nombre es obligatorio y no puede venir vacio")
        @Size(message = "El nombre no puede tener mas de 100 caracteres")
        String name,

        @NotBlank(message = "El código es obligatorio")
        @Size(message = "El nombre no puede tener mas de 25 caracteres")
        String code
){
    public CatalogueRequestDto{
        if (name != null) name = name.trim().toUpperCase();
        if (code != null) code =code.trim().toUpperCase();
    }
}