package com.sistema.notas.dto.core.grades;

public record CourseAverageDTO(
        Integer courseId,
        String courseName,
        Double finalScore,
        Double evaluatedPercentage,
        String status // "APROBADO" o "REPROBADO"
) {}