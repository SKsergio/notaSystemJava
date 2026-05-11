package com.sistema.notas.service.core;

import java.time.LocalDate;
import java.util.List;

import com.sistema.notas.dto.core.courseRegistration.CourseRegistrationRequestDTO;
import com.sistema.notas.dto.core.courseRegistration.CourseRegistrationResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.entity.enums.EnrollmentStatus;

public interface CourseRegistrationService {
    CourseRegistrationResponseDTO save(CourseRegistrationRequestDTO requestDTO);
    PaginateResponse<CourseRegistrationResponseDTO> obtenerRegistrosPaginados(int page, int size, String search , LocalDate fromDate, LocalDate  toDate);
    List<CourseRegistrationResponseDTO> listarToSelects();
    List<CourseRegistrationResponseDTO> findByCourse(Integer courseId);
    CourseRegistrationResponseDTO changeStatusRegistration(Integer id, EnrollmentStatus status);
}
