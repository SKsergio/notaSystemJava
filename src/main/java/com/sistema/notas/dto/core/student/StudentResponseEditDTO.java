package com.sistema.notas.dto.core.student;

import java.time.LocalDate;

import com.sistema.notas.entity.enums.GenderEnum;

public record StudentResponseEditDTO(
    Long id,
    String firstName,
    String secondName,
    String firstLastName,
    String secondLastName,
    String address,
    String phoneNumber,
    String email,
    GenderEnum gender,
    String routePhoto,
    LocalDate birthDate
) {
} 
