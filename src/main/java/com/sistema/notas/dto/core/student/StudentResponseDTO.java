package com.sistema.notas.dto.core.student;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sistema.notas.entity.enums.GenderEnum;

public record StudentResponseDTO(
        Integer id,
        String fullName,
        String address,
        String phoneNumber,
        String email,
        GenderEnum gender,
        String routePhoto,
        LocalDate birthDate,
        int age,
        String currentDegree,
        LocalDateTime createdAt) {
}
