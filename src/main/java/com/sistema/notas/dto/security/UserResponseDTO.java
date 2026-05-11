package com.sistema.notas.dto.security;

import com.sistema.notas.entity.enums.Role;

/**
 * Vista publica del usuario. NUNCA expone passwordHash.
 */
public record UserResponseDTO(
        Integer id,
        String email,
        Role role,
        Integer teacherId,
        Integer studentId
) {
}
