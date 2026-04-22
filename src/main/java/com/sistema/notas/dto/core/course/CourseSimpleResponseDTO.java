package com.sistema.notas.dto.core.course;

public record CourseSimpleResponseDTO(
    Integer id,
    String name,
    String code,
    // Integer totalStudents, luego
    int year
) {
}
