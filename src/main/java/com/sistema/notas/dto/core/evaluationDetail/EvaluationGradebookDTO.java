package com.sistema.notas.dto.core.evaluationDetail;

public record EvaluationGradebookDTO(
        // Datos del Alumno (Siempre vienen)
        Integer studentId,
        String studentFullName,
        String carnet,

        // Datos de la Nota (Pueden ser null)
        Integer evaluationDetailId,
        Double grade,
        String feedback
) {}