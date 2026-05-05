package com.sistema.notas.mapper.core;

import com.sistema.notas.dto.core.evaluationDetail.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import com.sistema.notas.dto.core.evaluations.EvaluationsResponseDTO;
import com.sistema.notas.dto.core.student.StudentSimpleResponseDTO;
import com.sistema.notas.entity.core.Evaluation;
import com.sistema.notas.entity.core.EvaluationDetail;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EvaluationDetailMapper {

    //de entidad a a response
    @Mapping(source = "evaluation.name", target = "evaluationName")
    @Mapping(source = "student.fullName", target = "studentName")
    @Mapping(source = "evaluation.course.name", target = "courseName")
    EvaluationDetailResponseDTO toResponseDTO(EvaluationDetail entity);

    //de request a entidad
    @Mapping(target = "evaluation", ignore = true)
    @Mapping(target = "student", ignore = true)
    EvaluationDetail toEntity(EvaluationDetailRequestDTO requestDTO);

    //de request a entidad para edicion
    EvaluationDetailEditResponseDTO toEditResponseDTO(EvaluationDetail entity);

    //actulizar entidad existente
    EvaluationDetail updateEntityFromDTO(EvaluationDetailEditRequestDTO requestDTO, @MappingTarget EvaluationDetail evaluationDetail);

    EvaluationDetailFullResponseDTO toFullResponseDTO(EvaluationDetail entity);

    default EvaluationsResponseDTO mapEvaluationtoDto(Evaluation evaluation){
        if (evaluation == null)
            return null;
        return new EvaluationsResponseDTO(
            evaluation.getId(),
            evaluation.getName(),
            evaluation.getDescription(),
            evaluation.getCourse().getName(),
            evaluation.getPercentage(),
            evaluation.getEndDate(),
            evaluation.getStatus(),
            evaluation.getDaysRemaning()
        );
    }

    default StudentSimpleResponseDTO mapStudentToSimpleDTO(com.sistema.notas.entity.core.Student student) {
        if (student == null)
            return null;
        return new StudentSimpleResponseDTO(
            student.getId(),
            student.getfullName(),
            student.getCarnet(),
            student.getEmail(),
            student.getRoutePhoto(),
            student.getAge()
        );
    }

}
