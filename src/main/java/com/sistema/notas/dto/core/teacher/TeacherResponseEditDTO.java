package com.sistema.notas.dto.core.teacher;

import com.sistema.notas.entity.enums.GenderEnum;

public record TeacherResponseEditDTO(
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
    String birthDate,
    String speciality,
    String dui
) {
} 
