package com.sistema.notas.config;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class TenantResolver implements CurrentTenantIdentifierResolver<Integer> {

    @Override
    public Integer resolveCurrentTenantIdentifier() {
        return 1;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }
}
