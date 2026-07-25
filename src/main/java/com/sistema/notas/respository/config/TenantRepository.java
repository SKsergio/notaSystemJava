package com.sistema.notas.respository.config;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sistema.notas.entity.Tenant;

public interface TenantRepository extends JpaRepository<Tenant, Integer>,  JpaSpecificationExecutor<Tenant>  {
    boolean existsByNit(String nit);
    boolean existsByNrc(String nrc);
    boolean existsByLegalName(String legalName);
    boolean existsByName(String legalName);

    //para edicion
    boolean existsByNitAndIdNot(String nit, Integer id);
    boolean existsByNrcAndIdNot(String nrc, Integer id);
    boolean existsByLegalNameAndIdNot(String legalName, Integer id);

}
