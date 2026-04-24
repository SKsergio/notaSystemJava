package com.sistema.notas.mapper.core;

import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.sistema.notas.dto.core.student.StudentFullResponseDTO;
import com.sistema.notas.dto.core.student.StudentRequestDTO;
import com.sistema.notas.dto.core.student.StudentResponseDTO;
import com.sistema.notas.entity.core.Student;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface StudentMapper {
    
    @Mapping(source = "routePhoto", target = "routePhoto", qualifiedByName = "mapPhotoUrl")
    StudentResponseDTO toResponseDTO(Student student);

    @Mapping(target = "routePhoto", ignore = true)
    Student toEntity(StudentRequestDTO responseDTO);

    @Mapping(target = "routePhoto", ignore = true)
    Student updateEntityFromDTO(StudentRequestDTO responseDTO, @MappingTarget Student student);

    @Mapping(source = "routePhoto", target = "routePhoto", qualifiedByName = "mapPhotoUrl")
    StudentFullResponseDTO toFullResponse(Student student);


    @Named("mapPhotoUrl")
    default String mapPhotoUrl(String routePhoto) {
        if (routePhoto == null || routePhoto.isEmpty()) {
            return null;
        }
        return "/photos/" + routePhoto;
    }
}