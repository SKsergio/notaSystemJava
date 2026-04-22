package com.sistema.notas.mapper.core;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.sistema.notas.dto.core.course.CourseFullResponseDTO;
import com.sistema.notas.dto.core.course.CourseRequestDTO;
import com.sistema.notas.dto.core.course.CourseResponseDTO;
import com.sistema.notas.entity.core.Course;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE )
public interface CourseMapper {

    //DE ENTIDAD A RESPONSE
    //asiganmos los nombres de las entidades a las variables del response
    @Mapping(source = "gradeDetail.fullName", target = "gradeDetailName")
    @Mapping(source = "subject.name", target = "subjectName")
    @Mapping(source = "teacher.fullName", target = "teacherName")
    @Mapping(source = "gradeDetail.year", target = "year")
    CourseResponseDTO toResponseDTO(Course entity);

    //DE REQUEST A ENTITY
    //aca vaciamos las entidades para que vayan vacios
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "period", ignore = true)
    @Mapping(target = "gradeDetail", ignore = true)
    Course toEntity(CourseRequestDTO requestDTO);

    //DE REQUEST A ENTITY
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "period", ignore = true)
    @Mapping(target = "gradeDetail", ignore = true)
    Course updateEntityFromDTO(CourseRequestDTO requestDTO, @MappingTarget Course course);

    CourseFullResponseDTO toFullResponseDTO(Course entity);

}
