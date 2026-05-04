package com.sistema.notas.dto.core.evaluationDetail;

public record EvaluationDetailResponseDTO(
    Integer id,
    Double grade,
    String feedback,
    String evaluationName,
    String studentName,
    String studentCode,
    String courseName
    ) {
}
