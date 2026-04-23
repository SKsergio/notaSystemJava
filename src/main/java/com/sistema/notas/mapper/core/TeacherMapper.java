package com.sistema.notas.mapper.core;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.sistema.notas.dto.core.course.CourseSimpleResponseDTO;
import com.sistema.notas.dto.core.gradeDetail.GradeDetailAssignedDTO;
import com.sistema.notas.dto.core.teacher.TeacherFullResponseDTO;
import com.sistema.notas.dto.core.teacher.TeacherRequestDTO;
import com.sistema.notas.dto.core.teacher.TeacherResponseDTO;
import com.sistema.notas.entity.core.Course;
import com.sistema.notas.entity.core.GradeDetail;
import com.sistema.notas.entity.core.Teacher;

@Mapper(
    componentModel = MappingConstants.ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface TeacherMapper {
    TeacherResponseDTO toResponseDTO(Teacher teacher);

    @Mapping(target = "routePhoto", ignore = true)
    Teacher toEntity(TeacherRequestDTO requestDTO);

    @Mapping(target = "routePhoto", ignore = true)
    Teacher updateEntityFromDTO(TeacherRequestDTO requestDTO, @MappingTarget Teacher teacher);

    TeacherFullResponseDTO toFullResponse(Teacher entity);

    @Mapping(source = "section.name", target = "sectionName")
    @Mapping(source = "degree.name", target = "degreeName")
    GradeDetailAssignedDTO toAssignedDTO(GradeDetail gradeDetail);

    @Mapping(source = "gradeDetail.year", target = "year") 
    CourseSimpleResponseDTO toCourseSimpleDTO(Course course);
}
