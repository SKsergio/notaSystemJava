package com.sistema.notas.dto.core.grades;

import java.util.List;

public record ReportCardDTO(
        Integer studentId,
        String studentFullName,
        String gradeName, // Ej: "Primer Año de Bachillerato"
        Double globalAverage, // Promedio de todas las materias
        List<CourseAverageDTO> courses
) {}