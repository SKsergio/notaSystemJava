package com.sistema.notas.dto.core.evaluations;

import java.time.LocalDate;

import com.sistema.notas.dto.core.course.CourseSimpleResponseDTO;

public record EvaluationFullResponseDTO(
    Integer id,
    String name,
    String description,
    LocalDate endDate,
    Integer status,
    Long daysRemaning,
    Double percentage,
    CourseSimpleResponseDTO course
) {    
}
