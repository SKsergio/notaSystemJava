package com.sistema.notas.dto.security.tenant;

public record UserTenantSummaryDTO(
    Integer tenantId,
    String tenantName,
    String roleName // Ej: "TEACHER", "MANAGER"
    ) {
    
}
