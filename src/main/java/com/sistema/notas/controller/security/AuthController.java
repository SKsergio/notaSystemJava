package com.sistema.notas.controller.security;

import com.sistema.notas.dto.security.LoginRequestDTO;
import com.sistema.notas.dto.security.LoginResponseDTO;
import com.sistema.notas.dto.security.RegisterRequestDTO;
import com.sistema.notas.dto.security.UserResponseDTO;
import com.sistema.notas.exceptions.UnauthorizedException;
import com.sistema.notas.service.security.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO dto) {
        return ResponseEntity.ok(authService.login(dto));
    }

    /** Devuelve el usuario actualmente autenticado. Util para refrescar perfil en el front. */
    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof UserDetails userDetails)) {
            throw new UnauthorizedException("Sesion invalida");
        }
        return ResponseEntity.ok(authService.findByEmail(userDetails.getUsername()));
    }

    /**
     * Logout: con JWT stateless el servidor no necesita hacer nada
     * (el cliente debe descartar el token). Lo dejamos como no-op
     * para que el frontend pueda llamarlo y registrar el evento si quiere.
     */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent().build();
    }

    /** Solo un ADMIN puede crear nuevos usuarios. */
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(dto));
    }
}
