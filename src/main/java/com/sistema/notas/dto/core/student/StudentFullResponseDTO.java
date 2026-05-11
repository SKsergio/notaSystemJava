package com.sistema.notas.dto.core.student;

import com.sistema.notas.entity.enums.GenderEnum;

import java.time.LocalDate;

public record StudentFullResponseDTO(
        Integer id,
        String fullName,
        String carnet,
        String email,
        int age,
        String routePhoto,
        GenderEnum gender,
        String address,
        String phoneNumber,
        LocalDate birthDate,
        String currentDegree,
        Integer gradeDetailId
        // grade details current
        // cum global(calculado en base a sus calificaciones)
        // historial de cursos con sus calificaciones
        // historial de grades con sus detalles
) {
}
