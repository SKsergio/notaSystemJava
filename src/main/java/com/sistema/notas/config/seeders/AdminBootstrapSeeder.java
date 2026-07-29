package com.sistema.notas.config.seeders;

import com.sistema.notas.entity.Tenant;
import com.sistema.notas.entity.security.Role;
import com.sistema.notas.entity.security.User;
import com.sistema.notas.entity.security.UserTenantAccess;
import com.sistema.notas.respository.config.TenantRepository;
import com.sistema.notas.respository.security.RoleRepository;
import com.sistema.notas.respository.security.UserRepository;
import com.sistema.notas.respository.security.UserTenantAccessRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AdminBootstrapSeeder {

    private final UserRepository userRepository;
    private final TenantRepository tenantRepository;
    private final RoleRepository roleRepository;
    private final UserTenantAccessRepository userTenantAccessRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.security.bootstrap.admin-email:admin@notas.local}")
    private String adminEmail;

    @Value("${app.security.bootstrap.admin-password:Admin123!}")
    private String adminPassword;

    @Transactional
    public void seed() {
        if (!userRepository.existsByEmail(adminEmail)) {
            System.out.println("👤 [SEEDER] Creando Administrador Inicial y asignando acceso al Tenant...");

            // 1. Crear Usuario Global
            User adminUser = new User();
            adminUser.setEmail(adminEmail);
            adminUser.setPasswordHash(passwordEncoder.encode(adminPassword));
            adminUser.setFirstLogin(false);
            adminUser.setSuperAdmin(true);
            User savedUser = userRepository.save(adminUser);

            // 2. Obtener Tenant de prueba (ID 1) y Rol ADMIN
            Tenant tenant = tenantRepository.findAll().stream().findFirst()
                    .orElseThrow(() -> new IllegalStateException("No existe ningun Tenant para vincular al Admin"));

            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new IllegalStateException("No se encontro el Rol ADMIN en la base de datos"));

            // 3. Crear Membresia en UserTenantAccess
            UserTenantAccess access = new UserTenantAccess();
            access.setUser(savedUser);
            access.setTenant(tenant);
            access.setRole(adminRole);

            userTenantAccessRepository.save(access);

            System.out.println("✅ [SEEDER] Administrador creado exitosamente (" + adminEmail + ") asignado al Tenant: " + tenant.getName());
        }
    }
}