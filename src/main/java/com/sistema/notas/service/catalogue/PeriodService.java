package com.sistema.notas.service.catalogue;

import com.sistema.notas.dto.catalogues.PeriodRequestDTO;
import com.sistema.notas.dto.catalogues.PeriodResponseDTO;
import com.sistema.notas.dto.catalogues.PeriodSimpleDTO;
import com.sistema.notas.dto.generics.PaginateResponse;

import java.time.LocalDate;
import java.util.List;

public interface PeriodService {
    PeriodResponseDTO save(PeriodRequestDTO period);

    PeriodResponseDTO update(Integer id, PeriodRequestDTO period);

    void delete(Integer id);

    PaginateResponse<PeriodResponseDTO> obtenerPeriodsPaginados(int page, int size, LocalDate fromDate, LocalDate  toDate);

    List<PeriodSimpleDTO> listartoSelects();

    PeriodResponseDTO obtenerPeriod(Integer id);
}
