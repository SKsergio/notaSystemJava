package com.sistema.notas.service.config;

import java.util.List;

import com.sistema.notas.dto.security.tenant.UserTenantSummaryDTO;

public interface UserTenantAccessService {
    List<UserTenantSummaryDTO> getUserTenants(Integer userId); 
}
