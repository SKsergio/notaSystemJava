package com.sistema.notas.controller.security;

import com.sistema.notas.config.security.model.CustomUserDetails;
import com.sistema.notas.dto.security.ChangePasswordRequestDTO;
import com.sistema.notas.dto.security.LoginRequestDTO;
import com.sistema.notas.dto.security.LoginResponseDTO;
import com.sistema.notas.dto.security.RegisterRequestDTO;
import com.sistema.notas.dto.security.UserResponseDTO;
import com.sistema.notas.dto.security.tenant.SwitchTenantRequest;
import com.sistema.notas.dto.security.tenant.UserTenantSummaryDTO;
import com.sistema.notas.exceptions.UnauthorizedException;
import com.sistema.notas.service.security.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    /** Endpoint publico: intercambia email/password por un JWT. */
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    /** Devuelve el usuario autenticado */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(Authentication authentication) {

        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {

            throw new UnauthorizedException("Sesion invalida");
        }

        return ResponseEntity.ok(
                authService.findByEmail(userDetails.getUsername()));
    }

    /** Cambio obligatorio de password */
    @PostMapping("/change-password")
    public ResponseEntity<Void> changePassword(
            @RequestBody ChangePasswordRequestDTO request) {

        authService.changePassword(request);

        return ResponseEntity.ok().build();
    }

    /**
     * Logout JWT stateless
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/me/tenants")
    public ResponseEntity<List<UserTenantSummaryDTO>> getMyTenants(Authentication authentication) {

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Integer currentUserId = userDetails.getId();
        List<UserTenantSummaryDTO> tenants = authService.getUserTenants(currentUserId);

        return ResponseEntity.ok(tenants);
    }

    /** Cambia el tenant activo del usuario autenticado y emite un nuevo JWT */
    @PostMapping("/switch-tenant")
    public ResponseEntity<LoginResponseDTO> switchTenant(
            Authentication authentication,
            @Valid @RequestBody SwitchTenantRequest request) {

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new UnauthorizedException("Sesion invalida");
        }

        return ResponseEntity.ok(
                authService.switchTenant(userDetails.getId(), request.targetTenantId()));
    }

    /** Solo ADMIN puede registrar usuarios */
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO dto) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(dto));
    }
}
