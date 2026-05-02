package com.sistema.notas.dto.core.course;

public record CourseEditResponseDTO(
        Integer id,
        double valorityUnity,
        Integer gradeDetailId,
        Integer periodId,
        Integer teacherId,
        Integer subjectId
) {
}
