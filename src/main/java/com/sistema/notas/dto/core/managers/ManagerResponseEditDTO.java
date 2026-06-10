package com.sistema.notas.dto.core.managers;

import com.sistema.notas.entity.enums.GenderEnum;

public record ManagerResponseEditDTO(
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
        String dui
) {
}
