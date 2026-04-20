package com.sistema.notas.dto.core.teacher;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.sistema.notas.entity.enums.GenderEnum;

public record TeacherResponseDTO(
        Integer id,
        String firstName,
        String secondName,
        String firstLastName,
        String secondLastName,
        String address,
        String phoneNumber,
        String email,
        GenderEnum gender,
        String routePhoto,
        LocalDate birthDate,
        int age,
        String speciality,
        String dui,
        LocalDateTime createdAt) {
}
