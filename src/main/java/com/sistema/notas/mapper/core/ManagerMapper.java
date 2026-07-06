package com.sistema.notas.mapper.core;

import com.sistema.notas.dto.core.course.CourseSimpleResponseDTO;
import com.sistema.notas.dto.core.gradeDetail.GradeDetailAssignedDTO;
import com.sistema.notas.dto.core.managers.*;
import com.sistema.notas.dto.core.student.AssignedStudentDTO;
import com.sistema.notas.dto.core.student.StudentSimpleResponseDTO;
import com.sistema.notas.entity.core.*;
import org.mapstruct.*;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface ManagerMapper {
    //manager responseDTO
    @Mapping(source = "routePhoto", target = "routePhoto", qualifiedByName = "mapPhotoUrl")
    ManagerResponseDTO toResponseDTO(Manager manager);

    //a entidad
    @Mapping(target = "routePhoto", ignore = true)
    Manager toEntity(ManagerRequestDTO requestDTO);

    //actualizar entidad existente
    @Mapping(target = "routePhoto", ignore = true)
    Manager updateEntityFromDTO(ManagerRequestUpdateDTO requestDTO, @MappingTarget Manager manager);

    @Mapping(source = "routePhoto", target = "routePhoto", qualifiedByName = "mapPhotoUrl")
    ManagerResponseEditDTO toResponseEditDTO(Manager manager);

    //response full
    @Mapping(source = "routePhoto", target = "routePhoto", qualifiedByName = "mapPhotoUrl")
    @Mapping(source = "studentRelations", target = "assignedStudents")
    ManagerFullResponseDTO toFullResponse(Manager entity);

    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "student.fullName", target = "fullName")
    @Mapping(source = "student.carnet", target = "carnet")
    AssignedStudentDTO toAssignedStudentDTO(ManagerStudents managerStudent);

    //simpleREsponse
    @Mapping(source = "routePhoto", target = "routePhoto", qualifiedByName = "mapPhotoUrl")
    ManagerSimpleResponseDTO toSimpleResponseDTO(Manager manager);


    @Named("mapPhotoUrl")
    default String mapPhotoUrl(String routePhoto) {
        if (routePhoto == null || routePhoto.isEmpty()) {
            return null;
        }
        return "/photos/" + routePhoto;
    }
}
