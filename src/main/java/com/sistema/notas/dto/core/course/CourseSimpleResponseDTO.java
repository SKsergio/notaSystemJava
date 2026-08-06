package com.sistema.notas.dto.core.course;

import com.sistema.notas.entity.enums.StatusEnum;

public record CourseSimpleResponseDTO(
    Integer id,
    String name,
    Integer gradeDetailId,
    String code,
    StatusEnum status,
    Integer totalStudents,
    int year
) {
}
