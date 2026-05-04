package com.sistema.notas.dto.core.evaluationDetail;

import com.sistema.notas.dto.core.evaluations.EvaluationsResponseDTO;
import com.sistema.notas.dto.core.student.StudentSimpleResponseDTO;

public record EvaluationDetailFullResponseDTO(
    Integer id,
    Double grade,
    String feedback,
    StudentSimpleResponseDTO student, 
    EvaluationsResponseDTO evaluation
) {
} 