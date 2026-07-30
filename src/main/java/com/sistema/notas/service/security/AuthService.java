package com.sistema.notas.service.security;

import com.sistema.notas.dto.security.LoginRequestDTO;
import com.sistema.notas.dto.security.LoginResponseDTO;
import com.sistema.notas.dto.security.RegisterRequestDTO;
import com.sistema.notas.dto.security.UserResponseDTO;
import com.sistema.notas.dto.security.tenant.UserTenantSummaryDTO;
import com.sistema.notas.entity.security.User;
import com.sistema.notas.entity.security.UserTenantAccess;
import com.sistema.notas.exceptions.BadRequestException;
import com.sistema.notas.exceptions.UnauthorizedException;
import com.sistema.notas.respository.security.UserRepository;
import com.sistema.notas.respository.security.UserTenantAccessRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Set;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.sistema.notas.dto.security.ChangePasswordRequestDTO;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserTenantAccessRepository userTenantAccessRepository;
    private final JwtService jwtService;

    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO dto) {

        // 1. Validación de Identidad (Autenticación Global)
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.email(), dto.password()));
        } catch (BadCredentialsException ex) {
            throw new UnauthorizedException("Credenciales invalidas");
        }

        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() -> new UnauthorizedException("Credenciales invalidas"));

        List<UserTenantAccess> accesses = userTenantAccessRepository.findByUserId(user.getId());

        if (accesses.isEmpty()) {
            if (!user.isSuperAdmin()) {
                throw new UnauthorizedException("El usuario no tiene acceso a ninguna institución.");
            }

            // Superadmin puro: no pertenece a ningun tenant, se emite un token a nivel de plataforma.
            String platformToken = jwtService.generateToken(user, null, null, Set.of(), true);
            return new LoginResponseDTO(
                    platformToken,
                    "Bearer",
                    jwtService.getExpirationMs(),
                    toResponse(user),
                    user.isFirstLogin());
        }

        UserTenantAccess selectedAccess;

        // 3. Lógica de Enrutamiento SaaS
        if (accesses.size() == 1) {
            // Caso A: El usuario solo pertenece a 1 colegio (Como tu Administrador Seed
            selectedAccess = accesses.get(0);
        } else {
            // Caso B: El usuario pertenece a Varios Colegios.
            // TODO: Aquí en el futuro deberíamos devolver un DTO pidiendo al Frontend que
            // muestre
            // una pantalla de "Seleccionar Colegio". Por ahora, para no romper tu frontend
            // de Vue,
            // tomaremos el primer colegio por defecto.
            selectedAccess = accesses.get(0);
        }

        // 4. Calcular Permisos Efectivos (Rol Base + Extras - Revocados)
        Set<String> effectivePermissions = calculateEffectivePermissions(selectedAccess);

        // 5. ¡LA MAGIA! Generar el JWT sellado con el Tenant y los Permisos Exactos
        String token = jwtService.generateToken(
                user,
                selectedAccess.getTenant().getId(),
                selectedAccess.getRole().getName(),
                effectivePermissions,
                user.isSuperAdmin());

        return new LoginResponseDTO(
                token,
                "Bearer",
                jwtService.getExpirationMs(),
                toResponse(user),
                user.isFirstLogin());
    }

    @Transactional(readOnly = true)
    public List<UserTenantSummaryDTO> getUserTenants(Integer userId) {
        return userTenantAccessRepository.findTenantsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public LoginResponseDTO switchTenant(Integer userId, Integer targetTenantId) {

        // 1. Validar que el usuario pertenece al tenant destino
        UserTenantAccess access = userTenantAccessRepository
                .findByUserIdAndTenantId(userId, targetTenantId)
                .orElseThrow(() -> new AccessDeniedException(
                    "Acceso denegado: No perteneces al colegio especificado."
                ));

        Set<String> effectivePermissions = calculateEffectivePermissions(access);

        User user = access.getUser();
        String newToken = jwtService.generateToken(
                user,
                access.getTenant().getId(),
                access.getRole().getName(),
                effectivePermissions,
                user.isSuperAdmin()
        );

        return new LoginResponseDTO(
                newToken,
                "Bearer",
                jwtService.getExpirationMs(),
                toResponse(user),
                user.isFirstLogin());
    }
    
    /**
     * Calcula la lista final de permisos aplicando la fórmula de excepciones.
     */
    private Set<String> calculateEffectivePermissions(UserTenantAccess access) {
        // A. Obtener permisos base del Rol
        Set<String> effective = access.getRole().getPermissions().stream()
                .map(p -> p.getCode())
                .collect(java.util.stream.Collectors.toSet());

        if (access.getGrantedPermissions() != null) {
            access.getGrantedPermissions().forEach(p -> effective.add(p.getCode()));
        }

        if (access.getRevokedPermissions() != null) {
            access.getRevokedPermissions().forEach(p -> effective.remove(p.getCode()));
        }

        return effective;
    }

    @Transactional
    public UserResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.email())) {
            throw new BadRequestException("Ya existe un usuario con el correo: " + dto.email());
        }

        User user = new User();
        user.setEmail(dto.email());
        user.setPasswordHash(passwordEncoder.encode(dto.password()));

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Sesion invalida"));
        return toResponse(user);
    }

    // creo que esto lo voy a pasar a un mapper
    private UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getEmail());
    }

    @Transactional
    public void changePassword(ChangePasswordRequestDTO request) {

        Authentication auth = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UnauthorizedException("Usuario no encontrado"));

        user.setPasswordHash(
                passwordEncoder.encode(request.newPassword()));

        user.setFirstLogin(false);

        userRepository.save(user);
    }
}
