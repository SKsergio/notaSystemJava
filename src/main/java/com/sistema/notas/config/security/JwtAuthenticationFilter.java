package com.sistema.notas.config.security;

import com.sistema.notas.config.security.model.CustomUserDetails;
import com.sistema.notas.service.security.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Lee el header Authorization: Bearer <jwt>, valida el token y, si es correcto,
 * pone la Authentication en el SecurityContext.
 *
 * Si no hay token o es invalido, NO escribe la respuesta de error: deja que
 * el resto del filter chain decida (que termina en authenticationEntryPoint
 * configurado en SecurityConfig si la ruta requiere autenticacion).
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        // Saltarse preflight CORS
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader(AUTH_HEADER);
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(BEARER_PREFIX.length()).trim();
        if (token.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = jwtService.parse(token);
            String email = claims.getSubject();

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                Integer uid = claims.get("uid", Integer.class);
                Integer tenantId = claims.get("tenantId", Integer.class);
                String role = claims.get("role", String.class);
                Boolean superAdmin = claims.get("superAdmin", Boolean.class);

                List<String> permissions = claims.get("permissions", List.class);
                List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                if (role != null) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                }
                if (Boolean.TRUE.equals(superAdmin)) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_SUPERADMIN"));
                }
                if (permissions != null) {
                    permissions.forEach(p -> authorities.add(new SimpleGrantedAuthority(p)));
                }

                CustomUserDetails userDetails = new CustomUserDetails(
                        uid,
                        email,
                        "",
                        tenantId,
                        role,
                        authorities,
                        true
                );

                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        } catch (JwtException | IllegalArgumentException ex) {
            // Token invalido / expirado: limpiamos el contexto y dejamos seguir.
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}
