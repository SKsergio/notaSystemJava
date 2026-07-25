package com.sistema.notas.service.config.impl;

import com.sistema.notas.mapper.PageMapper;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.dto.security.tenant.TenantRequestDTO;
import com.sistema.notas.dto.security.tenant.TenantResponseDTO;
import com.sistema.notas.dto.security.tenant.TenantSimpleResponseDTO;
import com.sistema.notas.entity.Tenant;
import com.sistema.notas.exceptions.BadRequestException;
import com.sistema.notas.exceptions.ResourceNotFoundException;
import com.sistema.notas.mapper.security.TenantMapper;
import com.sistema.notas.respository.config.TenantRepository;
import com.sistema.notas.service.config.TenantService;
import com.sistema.notas.specifications.catalogue.CatalogoSpecification;
import com.sistema.notas.specifications.config.tenant.TenantSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TenantServiceImpl implements TenantService {

    private final PageMapper pageMapper;
    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;

    @Override
    public TenantResponseDTO save(TenantRequestDTO tenant) {
        if (tenantRepository.existsByNit(tenant.nit())) {
            throw new BadRequestException("Ya existe una institucion con ese nit");
        }
        if (tenantRepository.existsByNrc(tenant.nrc())) {
            throw new BadRequestException("Ya existe una institucion con ese nrc");
        }
        if (tenantRepository.existsByLegalName(tenant.legalName())) {
            throw new BadRequestException("Ya existe una institucion con ese nombre legal");
        }
        if (tenantRepository.existsByName(tenant.name())) {
            throw new BadRequestException("Ya existe un institucion con ese nombre");
        }

        Tenant entity = tenantMapper.toEntity(tenant);
        Tenant saved = tenantRepository.save(entity);
        return tenantMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public TenantResponseDTO update(Integer id, TenantRequestDTO tenant) {
        Tenant tenandFind = tenantRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No existe ninguna institucion con el id: " + id));

        // validando duplicidad
        if (tenantRepository.existsByNitAndIdNot(tenant.nit(), id)) {
            throw new BadRequestException("Ya hay una institucion registrada con el nit: " + tenant.nit());
        }
        if (tenantRepository.existsByNrcAndIdNot(tenant.nrc(), id)) {
            throw new BadRequestException("Ya hay una institucion registrada con el nrc: " + tenant.nrc());
        }
        if (tenantRepository.existsByLegalNameAndIdNot(tenant.legalName(), id)) {
            throw new BadRequestException(
                    "Ya hay una institucion registrada con el nombre legal: " + tenant.legalName());
        }

        tenantMapper.updateEntityFromDTO(tenant, tenandFind);
        return tenantMapper.toResponseDTO(tenandFind);
    }

    @Override
    public void delete(Integer id) {
        Tenant tenandFind = tenantRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No existe ninguna institucion con el id: " + id));

        tenantRepository.delete(tenandFind);
    }

    @Override
    public PaginateResponse<TenantResponseDTO> getTenantPaginate(int page, int size, String search, LocalDate fromDate, LocalDate toDate) {
                Pageable pagable = PageRequest.of(page, size);

        Specification<Tenant> filtros = Specification
                .where(TenantSpecification.search(search))
                .and(CatalogoSpecification.createdBetween(fromDate, toDate));

        Page<Tenant> tenants = tenantRepository.findAll(filtros, pagable);

        return pageMapper.toPaginateResponse(tenants, 
            tenantMapper::toResponseDTO
        );
    }

    @Override
    public TenantResponseDTO findById(Integer id) {
        Tenant tenandFind = tenantRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No existe ninguna institucion con el id: " + id));

        return tenantMapper.toResponseDTO(tenandFind);
    }

    @Override
    public List<TenantSimpleResponseDTO> listarToSelects() {
        List<Tenant> tenants = tenantRepository.findAll();
        return tenantMapper.toSimpleResponseTenantList(tenants);
       
    }

}
