package com.sistema.notas.dto.security.tenant;

public record TenantSimpleResponseDTO(
    Integer id,
    String name,
    String nit
) {
}
