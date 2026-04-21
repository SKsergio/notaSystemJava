package com.sistema.notas.dto.core.teacher;

public record TeacherSimpleResponseDTO(
        Integer id,
        String firstName,
        String firstLastName,
        String email,
        String routePhoto,
        int age,
        String dui
       ) {
}
