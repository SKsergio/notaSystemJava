package com.sistema.notas.config.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sistema.notas.config.security.model.CustomUserDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class TenantFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // 1. OMITIR RUTAS PÚBLICAS
        if (isPublicRoute(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 2. EL TENANT SALE DEL JWT AUTENTICADO, NUNCA DE UN HEADER QUE EL CLIENTE CONTROLA
            Integer tenantId = resolveAuthenticatedTenantId();

            if (tenantId == null) {
                buildErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "No se pudo determinar el tenant del usuario autenticado.");
                return;
            }

            // 3. SETEAR EL CONTEXTO Y CONTINUAR
            TenantContext.setCurrentTenant(tenantId);
            filterChain.doFilter(request, response);

        } finally {
            // 4. LIMPIEZA OBLIGATORIA
            TenantContext.clear();
        }
    }

    private Integer resolveAuthenticatedTenantId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails userDetails) {
            return userDetails.getTenantId();
        }
        return null;
    }

    private boolean isPublicRoute(String path) {
        return path.contains("/swagger-ui") ||
               path.contains("/v3/api-docs") ||
               path.contains("/photos/") ||
               path.contains("/api/auth/") || // Aquí entrará el login más adelante
               path.contains("/api/core/tenants"); // Gestion global de tenants (solo super admin), no pertenece a ningun tenant
    }

    private void buildErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, Object> errorDetails = new HashMap<>();
        errorDetails.put("timestamp", LocalDateTime.now().toString());
        errorDetails.put("status", status);
        errorDetails.put("error", "Tenant Identifier Required");
        errorDetails.put("message", message);

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(errorDetails));
    }
}
