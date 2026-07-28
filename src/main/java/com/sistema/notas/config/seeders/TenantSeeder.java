package com.sistema.notas.config.seeders;

import org.springframework.stereotype.Component;

import com.sistema.notas.entity.Tenant;
import com.sistema.notas.respository.config.TenantRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TenantSeeder {
    private final TenantRepository tenantRepository;

    public void seed(){
        if(tenantRepository.count() == 0){
            System.out.println("[SEEDER] - Iniciando el proceso de seeders para la tabla Tenant");
            Tenant tenant = new Tenant();
            tenant.setName("Colegio San Francisco (Pruebas)");
            tenant.setNit("00000000000000");
            tenant.setNrc("000001");
            tenant.setLegalName("Sistemas Educativos S.A.");
            tenant.setAddress("San Salvador, El Salvador");
            tenant.setDomainOrSlug("sanfrancisco");
            tenant.setActive(true);

            tenantRepository.save(tenant);
            System.out.println("✅ [SEEDER] Tenant creado.");
        }
    }
}
