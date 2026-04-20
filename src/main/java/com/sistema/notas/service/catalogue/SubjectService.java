package com.sistema.notas.service.catalogue;

import com.sistema.notas.dto.catalogues.CatalogSimpleResponseDTO;
import com.sistema.notas.dto.catalogues.SubjectRequestDTO;
import com.sistema.notas.dto.catalogues.SubjectResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;

import java.time.LocalDate;
import java.util.List;

public interface SubjectService {
    SubjectResponseDTO save(SubjectRequestDTO subject);

    SubjectResponseDTO update(Integer id, SubjectRequestDTO subject);

    void delete(Integer id);

    PaginateResponse<SubjectResponseDTO> obtenerSubjectsPaginadas(int page, int size, String search,
            LocalDate fromDate, LocalDate  toDate);

    List<CatalogSimpleResponseDTO> obtenerSubjectSelect();

    SubjectResponseDTO findById(Integer id);
}
