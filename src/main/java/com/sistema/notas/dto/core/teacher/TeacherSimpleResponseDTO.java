package com.sistema.notas.dto.core.teacher;

public record TeacherSimpleResponseDTO(
        Integer id,
        String fullName,
        String email,
        String routePhoto,
        int age,
        String dui
       ) {
}
