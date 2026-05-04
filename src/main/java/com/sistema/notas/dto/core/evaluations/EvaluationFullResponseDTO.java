package com.sistema.notas.dto.core.evaluations;

import java.time.LocalDate;

import com.sistema.notas.dto.core.course.CourseSimpleResponseDTO;
import com.sistema.notas.entity.enums.StatusEnum;

public record EvaluationFullResponseDTO(
    Integer id,
    String name,
    String description,
    LocalDate endDate,
    StatusEnum status,
    Long daysRemaning,
    Double percentage,
    CourseSimpleResponseDTO course
) {    
}
