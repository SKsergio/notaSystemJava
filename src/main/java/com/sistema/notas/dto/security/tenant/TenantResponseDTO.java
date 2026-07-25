package com.sistema.notas.dto.security.tenant;

public record TenantResponseDTO(
        Integer id,
        String name,
        String nit,
        String nrc,
        String legalName,
        String address,
        String domainOrSlug
) {
}
