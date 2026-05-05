package com.sistema.notas.mapper.core;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import com.sistema.notas.entity.core.Student;
import com.sistema.notas.dto.core.evaluationDetail.EvaluationDetailEditRequestDTO;
import com.sistema.notas.dto.core.evaluationDetail.EvaluationDetailEditResponseDTO;
import com.sistema.notas.dto.core.evaluationDetail.EvaluationDetailFullResponseDTO;
import com.sistema.notas.dto.core.evaluationDetail.EvaluationDetailRequestDTO;
import com.sistema.notas.dto.core.evaluationDetail.EvaluationDetailResponseDTO;
import com.sistema.notas.dto.core.evaluationDetail.EvaluationDetailSimpleResponseDTO;
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

    //simple Response
    @Mapping(source = "student.id", target = "studentId")
    @Mapping(source = "evaluation.id", target = "evaluationId")
    EvaluationDetailSimpleResponseDTO toSimpleResponseDTO(EvaluationDetail entity);

    //de request a entidad para edicion
    EvaluationDetailEditResponseDTO toEditResponseDTO(EvaluationDetail entity);

    //actulizar entidad existente
    EvaluationDetail updateEntityFromDTO(EvaluationDetailEditRequestDTO requestDTO, @MappingTarget EvaluationDetail evaluationDetail);

    //full response DTO
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

    default StudentSimpleResponseDTO mapStudentToSimpleDTO(Student student) {
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
