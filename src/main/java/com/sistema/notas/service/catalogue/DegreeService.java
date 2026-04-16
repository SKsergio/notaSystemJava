package com.sistema.notas.service.catalogue;

import com.sistema.notas.dto.catalogues.CatalogueRequestDto;
import com.sistema.notas.dto.catalogues.CatalogueResponseDTO;
import com.sistema.notas.dto.catalogues.PaginateResponse;
import com.sistema.notas.entity.catalogues.Degree;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface DegreeService {
    CatalogueResponseDTO save(CatalogueRequestDto degree);
    CatalogueResponseDTO update(Integer id, CatalogueRequestDto degree);
    void delete(Integer id);

    List<Degree> findAll();
    PaginateResponse<CatalogueResponseDTO> obtenerGradosPaginados(int page, int size);
    Optional<Degree> findById(Integer id);
    Optional<Degree> findByName(String name);
    Optional<Degree> findByCode(String code);
}
