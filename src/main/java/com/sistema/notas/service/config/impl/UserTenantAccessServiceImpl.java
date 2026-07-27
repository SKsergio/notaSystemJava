package com.sistema.notas.service.config.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.sistema.notas.dto.security.tenant.UserTenantSummaryDTO;
import com.sistema.notas.respository.security.UserTenantAccessRepository;
import com.sistema.notas.service.config.UserTenantAccessService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserTenantAccessServiceImpl implements UserTenantAccessService{
    
    private final UserTenantAccessRepository userTenantAccessRepository;

    @Override
    public List<UserTenantSummaryDTO> getUserTenants(Integer userId) {
       return userTenantAccessRepository.findTenantsByUserId(userId);
    }
    
}
