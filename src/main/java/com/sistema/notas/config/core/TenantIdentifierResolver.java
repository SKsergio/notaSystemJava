package com.sistema.notas.config.core;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver<Integer> {

    // ID reservado para tareas en segundo plano o el arranque de Spring Boot
    private static final Integer SYSTEM_TENANT_ID = -1;

    @Override
    public Integer resolveCurrentTenantIdentifier() {
        Integer currentTenant = TenantContext.getCurrentTenant();
        
        if (currentTenant != null) {
            return currentTenant;
        }
        
        // 2. Si NO hay petición web (Arranque de la app, Creación del Admin, etc.),
        // le inyectamos a Hibernate el Tenant del Sistema para que no colapse.
        return SYSTEM_TENANT_ID; 
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true; 
    }
}