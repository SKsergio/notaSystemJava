package com.sistema.notas.config.seeders;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final AdminBootstrapSeeder adminBootstrapSeeder;
    private final TenantSeeder tenantSeeder;
    private final PermissionAndRoleSeeder permissionAndRoleSeeder; // INYECTAR

    @Override
    public void run(String... args) throws Exception {
        System.out.println("⚙️ [SEEDER] Iniciando inyección de datos...");
        
        tenantSeeder.seed();
        permissionAndRoleSeeder.seed(); // EJECUTAR DESPUÉS DEL TENANT
        adminBootstrapSeeder.seed();
        
        System.out.println("🏁 [SEEDER] Base de datos lista.");
    }
}