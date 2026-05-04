package com.sistema.notas.dto.core.evaluationDetail;

public record EvaluationDetailEditResponseDTO(
    Integer id,
    Integer evaluationId,
    Integer studentId,
    Double grade,
    String feedback
) {
    
}
