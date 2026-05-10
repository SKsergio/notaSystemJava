package com.sistema.notas.service.security;

import com.sistema.notas.config.security.JwtProperties;
import com.sistema.notas.entity.security.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * Genera y valida JSON Web Tokens.
 * Algoritmo: HMAC-SHA256, secreto simetrico via JwtProperties.
 */
@Service
public class JwtService {

    private final JwtProperties props;
    private final SecretKey signingKey;

    public JwtService(JwtProperties props) {
        this.props = props;
        byte[] keyBytes = props.secret().getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            throw new IllegalStateException(
                    "El secreto JWT (app.security.jwt.secret) debe tener al menos 32 bytes (256 bits). " +
                            "Configure JWT_SECRET con un valor aleatorio largo.");
        }
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    /** Genera un token para el usuario dado, con su rol como claim. */
    public String generateToken(User user) {
        long now = System.currentTimeMillis();
        Date issuedAt = new Date(now);
        Date expiresAt = new Date(now + props.expirationMs());

        return Jwts.builder()
                .subject(user.getEmail())
                .issuer(props.issuer())
                .issuedAt(issuedAt)
                .expiration(expiresAt)
                .claims(Map.of(
                        "uid", user.getId(),
                        "role", user.getRole().name()
                ))
                .signWith(signingKey, Jwts.SIG.HS256)
                .compact();
    }

    /** Parsea y valida la firma/expiracion. Lanza JwtException si es invalido. */
    public Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(signingKey)
                .requireIssuer(props.issuer())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /** Devuelve el email (subject) si el token es valido, null en caso contrario. */
    public String extractEmailSafe(String token) {
        try {
            return parse(token).getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public long getExpirationMs() {
        return props.expirationMs();
    }
}
