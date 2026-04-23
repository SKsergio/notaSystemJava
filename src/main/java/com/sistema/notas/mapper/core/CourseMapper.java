package com.sistema.notas.mapper.core;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.sistema.notas.dto.catalogues.CatalogueSimpleResponseDTO;
import com.sistema.notas.dto.catalogues.PeriodSimpleResponseDto;
import com.sistema.notas.dto.core.course.CourseFullResponseDTO;
import com.sistema.notas.dto.core.course.CourseRequestDTO;
import com.sistema.notas.dto.core.course.CourseResponseDTO;
import com.sistema.notas.dto.core.gradeDetail.GradeDetailSimpleResponseDTO;
import com.sistema.notas.dto.core.teacher.TeacherSimpleResponseDTO;
import com.sistema.notas.entity.catalogues.Period;
import com.sistema.notas.entity.catalogues.Subject;
import com.sistema.notas.entity.core.Course;
import com.sistema.notas.entity.core.GradeDetail;
import com.sistema.notas.entity.core.Teacher;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourseMapper {

    // DE ENTIDAD A RESPONSE
    // asiganmos los nombres de las entidades a las variables del response
    @Mapping(source = "gradeDetail.fullName", target = "gradeDetailName")
    @Mapping(source = "subject.name", target = "subjectName")
    @Mapping(source = "teacher.fullName", target = "teacherName")
    @Mapping(source = "gradeDetail.year", target = "year")
    CourseResponseDTO toResponseDTO(Course entity);

    // DE REQUEST A ENTITY
    // aca vaciamos las entidades para que vayan vacios
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "period", ignore = true)
    @Mapping(target = "gradeDetail", ignore = true)
    Course toEntity(CourseRequestDTO requestDTO);

    // DE REQUEST A ENTITY
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "period", ignore = true)
    @Mapping(target = "gradeDetail", ignore = true)
    Course updateEntityFromDTO(CourseRequestDTO requestDTO, @MappingTarget Course course);

    @Mapping(source = "gradeDetail.year", target = "year")
    CourseFullResponseDTO toFullResponseDTO(Course entity);

    default CatalogueSimpleResponseDTO mapSubjectToSimpleDTO(Subject subject) {
        if (subject == null)
            return null;
        return new CatalogueSimpleResponseDTO(subject.getId(), subject.getName());
    }

    default PeriodSimpleResponseDto mapPeriodToSimpleDTO(Period period) {
        if (period == null)
            return null;
        return new PeriodSimpleResponseDto(period.getId(), period.getStartDate(), period.getEndDate());
    }

    default TeacherSimpleResponseDTO mapTeacherToSimpleDTO(Teacher teacher) {
        if (teacher == null)
            return null;
        return new TeacherSimpleResponseDTO(teacher.getId(), teacher.getfullName(),
                teacher.getEmail(), teacher.getRoutePhoto(), teacher.getAge(), teacher.getDui());
    }

    default GradeDetailSimpleResponseDTO mapGradeDetailToSimpleDTO(GradeDetail gradeDetail) {
        if (gradeDetail == null)
            return null;
        return new GradeDetailSimpleResponseDTO(
                gradeDetail.getId(),
                gradeDetail.getSection().getName(), 
                gradeDetail.getDegree().getName(),
                gradeDetail.getTutor().getfullName()
            );
    }

}
