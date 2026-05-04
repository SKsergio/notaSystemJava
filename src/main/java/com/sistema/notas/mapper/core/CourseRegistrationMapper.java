package com.sistema.notas.mapper.core;

import com.sistema.notas.dto.core.courseRegistration.CourseRegistrationRequestDTO;
import com.sistema.notas.dto.core.courseRegistration.CourseRegistrationResponseDTO;
import com.sistema.notas.entity.core.CourseRegistration;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseRegistrationMapper {
    
    //de entidad a response 
    @Mapping(source = "student.fullName", target = "studentName")
    @Mapping(source = "course.name", target = "courseName")
    CourseRegistrationResponseDTO toResponseDTO(CourseRegistration courseRegistration);

    // Mapeo directo de lista
    List<CourseRegistrationResponseDTO> toResponseDTOList(List<CourseRegistration> entities);

    //de request a entidad
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "student", ignore = true)
    CourseRegistration toEntity(CourseRegistrationRequestDTO courseRegistrationRequestDTO);
}
