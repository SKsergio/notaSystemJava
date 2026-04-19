package com.sistema.notas.service.catalogue;

import com.sistema.notas.dto.catalogues.CatalogSimpleResponseDTO;
import com.sistema.notas.dto.catalogues.CatalogueRequestDto;
import com.sistema.notas.dto.catalogues.CatalogueResponseDTO;
import com.sistema.notas.dto.catalogues.PaginateResponse;

import java.time.LocalDate;
import java.util.List;

public interface SectionService {
    CatalogueResponseDTO save(CatalogueRequestDto section);

    CatalogueResponseDTO update(Integer id, CatalogueRequestDto section);

    void delete(Integer id);

    PaginateResponse<CatalogueResponseDTO> obtenerSectionPaginadas(int page, int size, String search,
            LocalDate fromDate, LocalDate  toDate);

    List<CatalogSimpleResponseDTO> obtenerSectionSelect();

    CatalogueResponseDTO findById(Integer id);
}
