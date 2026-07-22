package com.sistema.notas.respository.config;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sistema.notas.entity.Tenant;

public interface TenantRepository extends JpaRepository<Tenant, Integer>,  JpaSpecificationExecutor<Tenant>  {
    
}
