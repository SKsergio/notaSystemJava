package com.sistema.notas.dto.catalogues;
import java.time.LocalDateTime;

public record SubjectResponseDTO(
        Integer id,
        String name,
        String code,
        LocalDateTime createdAt,
        String description
) {}