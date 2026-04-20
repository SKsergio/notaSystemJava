package com.sistema.notas.dto.core.teacher;

import java.time.LocalDateTime;


public record TeacherSimpleResposeDTO(
        Integer id,
        String firstName,
        String firstLastName,
        String email,
        String routePhoto,
        int age,
        String dui
       )  {
}
