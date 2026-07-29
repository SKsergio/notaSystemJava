package com.sistema.notas.dto.security.tenant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AssignUserToTenantRequestDTO(
    @NotNull(message = "El id del usuario es obligatorio")
    Integer userId,

    @NotBlank(message = "El rol es obligatorio")
    String roleName
) {
}
