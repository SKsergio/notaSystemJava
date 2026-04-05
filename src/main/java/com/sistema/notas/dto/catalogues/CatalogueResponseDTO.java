package com.sistema.notas.dto.catalogues;

import java.time.LocalDateTime;

public record CatalogueResponseDTO(
        Integer id,
        String name,
        String code,
        LocalDateTime createdAt
) {
}
