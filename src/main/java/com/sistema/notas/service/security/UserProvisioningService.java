package com.sistema.notas.service.security;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistema.notas.config.core.TenantContext;
import com.sistema.notas.entity.Tenant;
import com.sistema.notas.entity.security.Role;
import com.sistema.notas.entity.security.User;
import com.sistema.notas.entity.security.UserTenantAccess;
import com.sistema.notas.exceptions.BadRequestException;
import com.sistema.notas.exceptions.ResourceNotFoundException;
import com.sistema.notas.respository.config.TenantRepository;
import com.sistema.notas.respository.security.RoleRepository;
import com.sistema.notas.respository.security.UserRepository;
import com.sistema.notas.respository.security.UserTenantAccessRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserProvisioningService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TenantRepository tenantRepository;
    private final UserTenantAccessRepository userTenantAccessRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Aprovisiona la identidad global de un usuario y le otorga acceso al Tenant actual con un Rol específico.
     * * @String email Correo de la persona
     * @String roleName Nombre del rol ("MANAGER", "TEACHER", "STUDENT", etc.)
     * @Integer institutionalPersonId ID físico de la persona en la tabla correspondiente
     * @return UserTenantAccess La membresía creada o existente
     */
    @Transactional
    public UserTenantAccess provisionUserForCurrentTenant(String email, String roleName, Integer institutionalPersonId) {

        // 1. Obtener el Tenant activo del contexto HTTP
        Integer currentTenantId = requireCurrentTenant();

        Tenant currentTenant = tenantRepository.findById(currentTenantId)
                .orElseThrow(() -> new IllegalStateException("Tenant no encontrado con ID: " + currentTenantId));

        // 2. Obtener el Rol solicitado
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException("Error de configuracion: El Rol '" + roleName + "' no existe en el sistema."));

        // 3. Buscar usuario global o crearlo si es la primera vez que ingresa al SaaS
        User user = userRepository.findByEmail(email)
                .orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setPasswordHash(passwordEncoder.encode("123456")); // Credencial base inicial
                    newUser.setFirstLogin(true);
                    return userRepository.save(newUser);
                });

        // 4. Otorgar membresía en UserTenantAccess si aún no la tiene
        return userTenantAccessRepository.findByUserIdAndTenantId(user.getId(), currentTenantId)
                .orElseGet(() -> {
                    UserTenantAccess access = new UserTenantAccess();
                    access.setUser(user);
                    access.setTenant(currentTenant);
                    access.setRole(role);
                    access.setInstitutionalPersonId(institutionalPersonId);
                    return userTenantAccessRepository.save(access);
                });
    }

    /**
     * Desactiva la membresia (UserTenantAccess) de una persona institucional en el tenant actual
     * cuando esta es eliminada (soft-delete). No afecta al User global ni a sus otras membresias.
     */
    @Transactional
    public void deactivateAccessForCurrentTenant(Integer institutionalPersonId, String roleName) {
        Integer currentTenantId = requireCurrentTenant();

        userTenantAccessRepository
                .findByInstitutionalPersonIdAndTenantIdAndRoleName(institutionalPersonId, currentTenantId, roleName)
                .ifPresent(userTenantAccessRepository::delete);
    }

    /**
     * Asocia un usuario global ya existente a un tenant con un rol determinado.
     * Uso exclusivo de superadmin (asignar ADMINs u otros roles a un colegio directamente).
     * Si la membresia ya existia, actualiza el rol (operacion idempotente de "asegurar rol X").
     */
    @Transactional
    public UserTenantAccess assignExistingUserToTenant(Integer userId, Integer tenantId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe ningun usuario con el id: " + userId));

        Tenant tenant = tenantRepository.findById(tenantId)
                .orElseThrow(() -> new ResourceNotFoundException("No existe ningun tenant con el id: " + tenantId));

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new BadRequestException("El rol '" + roleName + "' no existe."));

        UserTenantAccess access = userTenantAccessRepository.findByUserIdAndTenantId(userId, tenantId)
                .orElseGet(UserTenantAccess::new);

        access.setUser(user);
        access.setTenant(tenant);
        access.setRole(role);
        return userTenantAccessRepository.save(access);
    }

    private Integer requireCurrentTenant() {
        Integer currentTenantId = TenantContext.getCurrentTenant();
        if (currentTenantId == null) {
            throw new IllegalStateException("Error de seguridad: No existe un Tenant configurado en el contexto de la peticion.");
        }
        return currentTenantId;
    }
}