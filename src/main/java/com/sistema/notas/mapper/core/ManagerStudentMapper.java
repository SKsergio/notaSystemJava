package com.sistema.notas.mapper.core;

import com.sistema.notas.dto.core.evaluationDetail.EvaluationDetailEditRequestDTO;
import com.sistema.notas.dto.core.managerStudents.ManagerStudentRequestDTO;
import com.sistema.notas.dto.core.managerStudents.ManagerStudentRequestEditDTO;
import com.sistema.notas.dto.core.managerStudents.ManagerStudentResponseDTO;
import com.sistema.notas.entity.core.EvaluationDetail;
import com.sistema.notas.entity.core.ManagerStudents;
import org.mapstruct.*;

import java.util.List;


@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ManagerStudentMapper {

    // De entidad a response
    @Mapping(source = "student.fullName", target = "studentName")
    @Mapping(source = "manager.fullName", target = "managerName")
    ManagerStudentResponseDTO toResponseDTO(ManagerStudents managerStudents);

    // De request a entidad (El Enum a String se mapea solo)
    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "student", ignore = true)
    ManagerStudents toEntity(ManagerStudentRequestDTO managerStudentRequestDTO);

    //para editar
//    ManagerStudents updateEntityFromDTO(ManagerStudentRequestEditDTO requestDTO, @MappingTarget ManagerStudents managerStudents);

    List<ManagerStudentResponseDTO> toResponseDTOList(List<ManagerStudents> entities);

    // Para actualizar
    @Mapping(target = "manager", ignore = true)
    @Mapping(target = "student", ignore = true)
    void updateEntityFromDTO(ManagerStudentRequestEditDTO requestDTO, @MappingTarget ManagerStudents managerStudents);
}
