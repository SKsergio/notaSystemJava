package com.sistema.notas.dto.core.managers;

import com.sistema.notas.entity.enums.GenderEnum;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record ManagerResponseDTO(
        Integer id,
        String fullName,
        String address,
        String phoneNumber,
        String email,
        GenderEnum gender,
        String routePhoto,
        LocalDate birthDate,
        int age,
        String dui,
        LocalDateTime createdAt
) {
}
