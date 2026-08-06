package com.sistema.notas.mapper.core;

import com.sistema.notas.dto.catalogues.PeriodSimpleResponseDto;
import com.sistema.notas.dto.core.evaluations.EvaluationEditResponse;
import com.sistema.notas.entity.catalogues.Period;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import com.sistema.notas.dto.core.course.CourseSimpleResponseDTO;
import com.sistema.notas.dto.core.evaluations.EvaluationFullResponseDTO;
import com.sistema.notas.dto.core.evaluations.EvaluationRequestDTO;
import com.sistema.notas.dto.core.evaluations.EvaluationsResponseDTO;
import com.sistema.notas.entity.core.Course;
import com.sistema.notas.entity.core.Evaluation;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EvaluationsMapper {
    //de entidad a response
    @Mapping(source = "course.name", target = "courseName")
    EvaluationsResponseDTO toResponseDTO(Evaluation entity);

    //de entidad a editResponse para editar
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "period.id", target = "periodId")
    EvaluationEditResponse toEditResponseDTO(Evaluation entity);

    //de request a entidad
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "period", ignore = true)
    Evaluation toEntity(EvaluationRequestDTO requestDTO);

    //de request a entidad para edicion
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "period", ignore = true)
    Evaluation updateEntityFromDTO(EvaluationRequestDTO requestDTO, @MappingTarget Evaluation evaluation);

    EvaluationFullResponseDTO toFullResponseDTO(Evaluation entity);

    default PeriodSimpleResponseDto mapPeriodToSimpleDTO(Period period) {
        if (period == null)
            return null;
        return new PeriodSimpleResponseDto(period.getId(), period.getStartDate(), period.getEndDate());
    }

    default CourseSimpleResponseDTO mapCourseToSimpleDTo(Course course) {
        if (course == null)
            return null;
        return new CourseSimpleResponseDTO(
            course.getId(),
            course.getName(),
            course.getGradeDetail().getId(),
            course.getCode(),
            course.getStatus(),
            course.getTotalStudents(),
            course.getGradeDetail().getYear()
        );
    }
}
