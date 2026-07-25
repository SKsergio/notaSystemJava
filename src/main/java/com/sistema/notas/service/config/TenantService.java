package com.sistema.notas.service.config;

import java.time.LocalDate;
import java.util.List;

import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.dto.security.tenant.TenantRequestDTO;
import com.sistema.notas.dto.security.tenant.TenantResponseDTO;
import com.sistema.notas.dto.security.tenant.TenantSimpleResponseDTO;

public interface TenantService {
    TenantResponseDTO save(TenantRequestDTO tenant);
    TenantResponseDTO update(Integer id, TenantRequestDTO tenant);
    void delete(Integer id);
    PaginateResponse<TenantResponseDTO> getTenantPaginate(int page, int size, String search,
        LocalDate fromDate, LocalDate toDate);
    TenantResponseDTO findById(Integer id);
    List<TenantSimpleResponseDTO> listarToSelects(); 
}
