package com.sistema.notas.dto.core.evaluations;

import com.sistema.notas.entity.enums.StatusEnum;

import java.time.LocalDate;

public record EvaluationsResponseDTO(
    Integer id,
    String name,
    String description,
    String courseName,
    Double percentage,
    LocalDate endDate,
    StatusEnum status,
    Long daysRemaning
){
}
