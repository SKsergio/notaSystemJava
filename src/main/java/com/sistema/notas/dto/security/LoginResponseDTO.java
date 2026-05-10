package com.sistema.notas.dto.security;

/**
 * Respuesta de un login exitoso. Nunca incluye el password hash.
 */
public record LoginResponseDTO(
        String token,
        String tokenType,
        long expiresInMs,
        UserResponseDTO user
) {
}
