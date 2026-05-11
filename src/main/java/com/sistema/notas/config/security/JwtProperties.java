package com.sistema.notas.config.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propiedades de seguridad/JWT, cargadas desde application.properties
 * bajo el prefijo app.security.jwt.*
 */
@ConfigurationProperties(prefix = "app.security.jwt")
public record JwtProperties(
        String secret,
        long expirationMs,
        String issuer
) {
}
