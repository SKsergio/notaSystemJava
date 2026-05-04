package com.sistema.notas.service.core;


import com.sistema.notas.dto.core.degreeEnrollment.DegreeEnrollmentRequestDTO;
import com.sistema.notas.dto.core.degreeEnrollment.DegreeEnrollmentResponseDTO;
import com.sistema.notas.dto.core.degreeEnrollment.DegreeEnrollmentSimpleResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.entity.enums.EnrollmentStatus;

import java.time.LocalDate;
import java.util.List;

public interface DegreeEnrollmentService {

    DegreeEnrollmentResponseDTO save(DegreeEnrollmentRequestDTO degreeEnrollmentDTO);
    void delete(Integer id);
    PaginateResponse<DegreeEnrollmentResponseDTO> obtenerEnrtollmentPaginados(int page, int size, String search , LocalDate fromDate, LocalDate  toDate);
    List<DegreeEnrollmentSimpleResponseDTO> listarToSelects();
    DegreeEnrollmentResponseDTO changeStatusEnrollment(Integer id, EnrollmentStatus status);
}
