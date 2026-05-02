package com.sistema.notas.service.core;

import java.time.LocalDate;
import java.util.List;

import com.sistema.notas.dto.core.course.*;
import com.sistema.notas.dto.generics.PaginateResponse;

public interface CourseService {
    //funcion para aprobarCurso por alumno
    //funcion para reprobarse
    //funcion para evaluar notas por curso
    //funcion para dar por finalizado

    CourseResponseDTO save(CourseRequestDTO courseDto);
    CourseResponseDTO update(Integer id, CourseRequestDTO courseDto);
    void delete(Integer id);
    PaginateResponse<CourseResponseDTO> obtenerCoursePaginados(int page, int size, String search ,LocalDate fromDate, LocalDate  toDate);
    List<CourseSimpleResponseDTO> listarToSelects();
    CourseFullResponseDTO obtenerOneCourse(Integer id);
    CourseEditResponseDTO obtenerOneCourseEdit(Integer id);
}
