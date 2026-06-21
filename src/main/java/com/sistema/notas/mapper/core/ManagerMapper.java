package com.sistema.notas.mapper.core;

import com.sistema.notas.dto.core.course.CourseSimpleResponseDTO;
import com.sistema.notas.dto.core.gradeDetail.GradeDetailAssignedDTO;
import com.sistema.notas.dto.core.managers.*;
import com.sistema.notas.entity.core.Course;
import com.sistema.notas.entity.core.GradeDetail;
import com.sistema.notas.entity.core.Manager;
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
    ManagerFullResponseDTO toFullResponse(Manager entity);

//    @Mapping(source = "section.name", target = "sectionName")
//    @Mapping(source = "degree.name", target = "degreeName")
//    GradeDetailAssignedDTO toAssignedDTO(GradeDetail gradeDetail);

//    @Mapping(source = "gradeDetail.year", target = "year")
//    CourseSimpleResponseDTO toCourseSimpleDTO(Course course);

    @Named("mapPhotoUrl")
    default String mapPhotoUrl(String routePhoto) {
        if (routePhoto == null || routePhoto.isEmpty()) {
            return null;
        }
        return "/photos/" + routePhoto;
    }
}
