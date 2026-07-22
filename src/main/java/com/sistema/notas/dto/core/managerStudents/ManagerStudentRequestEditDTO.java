package com.sistema.notas.dto.core.managerStudents;

import com.sistema.notas.entity.enums.RelationTypeEnum;
import jakarta.validation.constraints.NotNull;

public record ManagerStudentRequestEditDTO(
        @NotNull(message = "El tipo de relación es obligatorio")
        RelationTypeEnum relationType,

        @NotNull(message = "Debe especificar si es contacto de emergencia")
        Boolean emergencyContact
) {
}
