package com.sistema.notas.mapper.core;


import com.sistema.notas.dto.core.degreeEnrollment.DegreeEnrollmentFullResponseDTO;
import com.sistema.notas.dto.core.degreeEnrollment.DegreeEnrollmentRequestDTO;
import com.sistema.notas.dto.core.degreeEnrollment.DegreeEnrollmentResponseDTO;
import com.sistema.notas.entity.core.DegreeEnrollment;
import org.mapstruct.*;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DegreeEnrollmentMapper {

    //de entidad a response
    @Mapping(source = "student.fullName", target = "studentName")
    @Mapping(source = "gradeDetail.fullName", target = "degreeName")
    DegreeEnrollmentResponseDTO toResponseDTO(DegreeEnrollment degreeEnrollment);

    //de request a entidad
    @Mapping(target = "gradeDetail", ignore = true)
    @Mapping(target = "student", ignore = true)
    DegreeEnrollment  toEntity(DegreeEnrollmentRequestDTO degreeEnrollmentRequestDTO);

    //para actualizar
    @Mapping(target = "gradeDetail", ignore = true)
    @Mapping(target = "student", ignore = true)
    void updateEntityFromDTO(DegreeEnrollmentRequestDTO requestDTO, @MappingTarget DegreeEnrollment degreeEnrollment);

    @Mapping(source = "student.fullName", target = "studentName")
    @Mapping(source = "gradeDetail.fullName", target = "degreeName")
    DegreeEnrollmentFullResponseDTO toFullResponseDTO(DegreeEnrollment degreeEnrollment);
}
