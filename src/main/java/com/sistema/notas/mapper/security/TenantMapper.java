package com.sistema.notas.mapper.security;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.sistema.notas.dto.security.tenant.TenantRequestDTO;
import com.sistema.notas.dto.security.tenant.TenantResponseDTO;
import com.sistema.notas.dto.security.tenant.TenantSimpleResponseDTO;
import com.sistema.notas.entity.Tenant;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TenantMapper {
    TenantResponseDTO toResponseDTO(Tenant tenant);

    Tenant toEntity(TenantRequestDTO requestDTO);

    Tenant updateEntityFromDTO(TenantRequestDTO requestDTO, @MappingTarget Tenant tenant);


    default TenantSimpleResponseDTO toSimpleResponseTenant(Tenant tenant){
        if (tenant == null) {
            return null;
        }
        return new TenantSimpleResponseDTO(
            tenant.getId(),
            tenant.getName(),
            tenant.getNit()
        );
    }

    List<TenantSimpleResponseDTO> toSimpleResponseTenantList(List<Tenant> tenants);

}
