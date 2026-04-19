package com.sistema.notas.service.catalogue;

import com.sistema.notas.dto.catalogues.CatalogSimpleResponseDTO;
import com.sistema.notas.dto.catalogues.CatalogueRequestDto;
import com.sistema.notas.dto.catalogues.CatalogueResponseDTO;
import com.sistema.notas.dto.catalogues.PaginateResponse;
import com.sistema.notas.entity.catalogues.Degree;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DegreeService {
    CatalogueResponseDTO save(CatalogueRequestDto degree);

    CatalogueResponseDTO update(Integer id, CatalogueRequestDto degree);

    void delete(Integer id);

    PaginateResponse<CatalogueResponseDTO> obtenerGradosPaginados(int page, int size, String search,
            LocalDate  fromDate, LocalDate  toDate);

    List<CatalogSimpleResponseDTO> obtenerGradosParaSelect();

    CatalogueResponseDTO findById(Integer id);

    Optional<Degree> findByName(String name);

    Optional<Degree> findByCode(String code);
}
