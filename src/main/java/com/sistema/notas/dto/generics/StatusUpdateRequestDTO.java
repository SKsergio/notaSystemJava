package com.sistema.notas.dto.generics;
import com.sistema.notas.entity.enums.StatusEnum;
import jakarta.validation.constraints.NotNull;

public record StatusUpdateRequestDTO(
    @NotNull
    StatusEnum newStatus) {
} 
