package com.sistema.notas.dto.security.tenant;

public record UserTenantAssignmentResponseDTO(
    Integer userId,
    String userEmail,
    Integer tenantId,
    String tenantName,
    String roleName
) {
}
