package com.sistema.notas.mapper.core;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import com.sistema.notas.dto.core.course.CourseSimpleResponseDTO;
import com.sistema.notas.dto.core.gradeDetail.GradeDetailAssignedDTO;
import com.sistema.notas.dto.core.teacher.TeacherFullResponseDTO;
import com.sistema.notas.dto.core.teacher.TeacherRequestDTO;
import com.sistema.notas.dto.core.teacher.TeacherRequestUpdateDTO;
import com.sistema.notas.dto.core.teacher.TeacherResponseDTO;
import com.sistema.notas.dto.core.teacher.TeacherResponseEditDTO;
import com.sistema.notas.entity.core.Course;
import com.sistema.notas.entity.core.GradeDetail;
import com.sistema.notas.entity.core.Teacher;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TeacherMapper {
    //teacher responseDTO
    @Mapping(source = "routePhoto", target = "routePhoto", qualifiedByName = "mapPhotoUrl")
    TeacherResponseDTO toResponseDTO(Teacher teacher);

    //a entidad
    @Mapping(target = "routePhoto", ignore = true)
    Teacher toEntity(TeacherRequestDTO requestDTO);

    //actualizar entidad existente
    @Mapping(target = "routePhoto", ignore = true)
    Teacher updateEntityFromDTO(TeacherRequestUpdateDTO requestDTO, @MappingTarget Teacher teacher);

    @Mapping(source = "routePhoto", target = "routePhoto", qualifiedByName = "mapPhotoUrl")
    TeacherResponseEditDTO toResponseEditDTO(Teacher teacher);

    //response full
    @Mapping(source = "routePhoto", target = "routePhoto", qualifiedByName = "mapPhotoUrl")
    TeacherFullResponseDTO toFullResponse(Teacher entity);

    @Mapping(source = "section.name", target = "sectionName")
    @Mapping(source = "degree.name", target = "degreeName")
    GradeDetailAssignedDTO toAssignedDTO(GradeDetail gradeDetail);

    @Mapping(source = "gradeDetail.year", target = "year") 
    CourseSimpleResponseDTO toCourseSimpleDTO(Course course);

    @Named("mapPhotoUrl")
    default String mapPhotoUrl(String routePhoto) {
        if (routePhoto == null || routePhoto.isEmpty()) {
            return null;
        }
        return "/photos/" + routePhoto;
    }
}
