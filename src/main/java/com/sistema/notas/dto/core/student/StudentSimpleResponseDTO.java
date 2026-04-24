package com.sistema.notas.dto.core.student;

public record StudentSimpleResponseDTO(
        Integer id,
        String fullName,
        String email,
        String routePhoto,
        int age
    ) {
}
