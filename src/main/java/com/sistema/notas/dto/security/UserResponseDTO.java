package com.sistema.notas.dto.security;

/**
 * Vista publica del usuario. NUNCA expone passwordHash.
 */
public record UserResponseDTO(
        Integer id,
        String email
) {
}
