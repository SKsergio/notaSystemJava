package com.sistema.notas.dto.core.evaluations;

public record EvaluationSimpleResponse(
    Integer id,
    String name,
    Integer status,
    String courseName,
    Long daysRemaning
) {
    
}
