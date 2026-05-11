package com.sistema.notas.mapper.core;

import com.sistema.notas.dto.core.student.*;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

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

    @Mapping(source = "student.routePhoto", target = "routePhoto", qualifiedByName = "mapPhotoUrl")
    @Mapping(source = "currentDegree", target = "currentDegree")
    StudentResponseDTO toResponseDTOWithDegree(Student student, String currentDegree);

    @Mapping(source = "routePhoto", target = "routePhoto", qualifiedByName = "mapPhotoUrl")
    StudentSimpleResponseDTO toSimpleResponseDTO(Student student);

    @Mapping(target = "routePhoto", ignore = true)
    Student toEntity(StudentRequestDTO responseDTO);

    @Mapping(target = "routePhoto", ignore = true)
    Student updateEntityFromDTO(StudentEditRequestDTO responseDTO, @MappingTarget Student student);

    //response edit
    @Mapping(source = "routePhoto", target = "routePhoto", qualifiedByName = "mapPhotoUrl")
    StudentResponseEditDTO toResponseEditDto(Student student);

    //full response
    @Mapping(source = "student.routePhoto", target = "routePhoto", qualifiedByName = "mapPhotoUrl")
    @Mapping(source = "currentDegree", target = "currentDegree")
    @Mapping(source = "gradeDetailId", target = "gradeDetailId")
    StudentFullResponseDTO toFullResponse(Student student, String currentDegree, Integer gradeDetailId);

    @Named("mapPhotoUrl")
    default String mapPhotoUrl(String routePhoto) {
        if (routePhoto == null || routePhoto.isEmpty()) {
            return null;
        }
        return "/photos/" + routePhoto;
    }
}