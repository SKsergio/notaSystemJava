package com.sistema.notas.dto.core.managers;

public record ManagerSimpleResponseDTO(
        Integer id,
        String fullName,
        String email,
        String routePhoto,
        int age,
        String dui
) {
}
