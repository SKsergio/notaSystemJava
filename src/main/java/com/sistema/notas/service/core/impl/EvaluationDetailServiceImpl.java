package com.sistema.notas.service.core.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistema.notas.dto.core.evaluationDetail.EvaluationDetailEditRequestDTO;
import com.sistema.notas.dto.core.evaluationDetail.EvaluationDetailEditResponseDTO;
import com.sistema.notas.dto.core.evaluationDetail.EvaluationDetailFullResponseDTO;
import com.sistema.notas.dto.core.evaluationDetail.EvaluationDetailRequestDTO;
import com.sistema.notas.dto.core.evaluationDetail.EvaluationDetailResponseDTO;
import com.sistema.notas.dto.core.evaluationDetail.EvaluationDetailSimpleResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.entity.core.Evaluation;
import com.sistema.notas.entity.core.EvaluationDetail;
import com.sistema.notas.entity.core.Student;
import com.sistema.notas.entity.enums.EnrollmentStatus;
import com.sistema.notas.entity.enums.StatusEnum;
import com.sistema.notas.exceptions.BadRequestException;
import com.sistema.notas.mapper.PageMapper;
import com.sistema.notas.mapper.core.EvaluationDetailMapper;
import com.sistema.notas.mapper.core.EvaluationsMapper;
import com.sistema.notas.respository.core.CourseRegistrationRepository;
import com.sistema.notas.respository.core.EvaluationDetailRepository;
import com.sistema.notas.respository.core.EvaluationsRepository;
import com.sistema.notas.respository.core.StudentRepository;
import com.sistema.notas.service.core.EvaluationDetailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluationDetailServiceImpl implements EvaluationDetailService{

    private final EvaluationDetailRepository evaluationDetailRepository;
    private final EvaluationDetailMapper evaluationDetailMapper;
    private final PageMapper pageMapper;

    //ralaciones
    private final EvaluationsRepository evaluationsRepository;
    private final StudentRepository studentRepository;
    private final CourseRegistrationRepository courseRegistrationRepository;

    @Transactional
    @Override
    public EvaluationDetailResponseDTO save(EvaluationDetailRequestDTO requestDTO) {

        //validar que exista la evaluacion 
       Evaluation evaluation =  evaluationsRepository.findById(requestDTO.evaluationId())
            .orElseThrow(() -> new BadRequestException("Evaluación no encontrada con ID: " + requestDTO.evaluationId()));

            //validar que la evaluacion este abierta
        if (evaluation.getStatus() == StatusEnum.CLOSED) {
            throw new BadRequestException("No se pueden agregar calificaciones a una evaluación cerrada.");
        }

        //validar que el estudiante exista
        Student student = studentRepository.findById(requestDTO.studentId())
            .orElseThrow(() -> new BadRequestException("Estudiante no encontrado con ID: " + requestDTO.studentId()));


        //validar que el estudiante 
        if (evaluationDetailRepository.existsByStudentIdAndEvaluationId(requestDTO.studentId(), requestDTO.evaluationId())) {
            throw new BadRequestException("El estudiante ya tiene una calificación registrada para esta evaluación.");
        }

        //validar que el estudiante este inscrito en el curso de la evaluacion
        boolean isEnrolled = courseRegistrationRepository.existsByStudentIdAndCourseIdAndStatus(
            student.getId(), evaluation.getCourse().getId(), EnrollmentStatus.ACTIVE
        );

        if(!isEnrolled){
            throw new BadRequestException("El estudiante no está inscrito en el curso de la evaluación.");
        }

        EvaluationDetail evaluationDetail = evaluationDetailMapper.toEntity(requestDTO);
        evaluationDetail.setStudent(student);
        evaluationDetail.setEvaluation(evaluation);
        EvaluationDetail savedDetail = evaluationDetailRepository.save(evaluationDetail);
        return evaluationDetailMapper.toResponseDTO(savedDetail);
    }

    @Override
    public EvaluationDetailResponseDTO update(Integer id, EvaluationDetailEditRequestDTO requestDTO) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public PaginateResponse<EvaluationDetailResponseDTO> getDetailsPaginated(int page, int size, String search,
            LocalDate fromDate, LocalDate toDate) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getDetailsPaginated'");
    }

    @Override
    public EvaluationDetailFullResponseDTO getOneDetail(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOneDetail'");
    }

    @Override
    public EvaluationDetailEditResponseDTO getOneDetailEdit(Integer id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOneDetailEdit'");
    }

    @Override
    public List<EvaluationDetailSimpleResponseDTO> getGradesByEvaluation(Integer evaluationId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGradesByEvaluation'");
    }

    @Override
    public List<EvaluationDetailResponseDTO> getGradesByStudentAndCourse(Integer studentId, Integer courseId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getGradesByStudentAndCourse'");
    }

    @Override
    public Double getCurrentStudentAverage(Integer studentId, Integer courseId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentStudentAverage'");
    }
    
}
