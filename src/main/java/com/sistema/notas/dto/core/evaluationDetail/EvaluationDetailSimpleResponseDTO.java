package com.sistema.notas.dto.core.evaluationDetail;


public record EvaluationDetailSimpleResponseDTO(
    Integer id,
    Double grade,
    Integer studentId,
    Integer evaluationId
) {
    
}
