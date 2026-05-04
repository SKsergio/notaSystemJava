package com.sistema.notas.dto.core.evaluations;

import com.sistema.notas.entity.enums.StatusEnum;

public record EvaluationSimpleResponse(
    Integer id,
    String name,
    StatusEnum status,
    String courseName,
    Long daysRemaning
) {
    
}
