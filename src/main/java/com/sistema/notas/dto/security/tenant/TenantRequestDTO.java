package com.sistema.notas.dto.security.tenant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TenantRequestDTO(
    @NotBlank(message = "El nombre es obligatorio")
    String name,

    @NotBlank(message = "El nit es obligatorio")
    @Size(max = 14, message = "El nit no puede tener más de 14 caracteres")
    String nit,

    @NotBlank(message = "El nrc es obligatorio")
    @Size(max = 8, message = "El nrc no puede tener más de 8 caracteres")
    String nrc,

    @NotBlank(message = "El nombre legal es obligatorio")
    String legalName,

    @NotBlank(message = "La direccion es obligatorio")
    String address,

    // opcional: logo del colegio
    String logoUrl
) {
}
