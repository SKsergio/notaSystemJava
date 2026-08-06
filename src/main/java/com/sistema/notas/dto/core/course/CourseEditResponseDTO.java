package com.sistema.notas.dto.core.course;

public record CourseEditResponseDTO(
        Integer id,
        double valorityUnity,
        Integer gradeDetailId,
        Integer teacherId,
        Integer subjectId
) {
}
