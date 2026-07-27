package com.sistema.notas.dto.security.tenant;

import jakarta.validation.constraints.NotNull;

public record SwitchTenantRequest(
    @NotNull(message = "El ID del colegio destino es obligatorio")
    Integer targetTenantId
) {
    
}
