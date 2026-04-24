package com.sistema.notas.dto.core.student;

import com.sistema.notas.entity.enums.GenderEnum;

public record StudentFullResponseDTO(
        Integer id,
        // String firstName,
        String fullName,
        String email,
        String dui,
        int age,
        String routePhoto,
        GenderEnum gender,
        String address,
        String phoneNumber
        // grade details current
        // cum global(calculado en base a sus calificaciones)
        // historial de cursos con sus calificaciones
        // historial de grades con sus detalles
) {
}
