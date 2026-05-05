package com.sistema.notas.service.core.impl;

import java.time.LocalDate;
import java.util.List;

import com.sistema.notas.specifications.CatalogoSpecification;
import com.sistema.notas.specifications.EvaluationDetailSpecification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
import com.sistema.notas.exceptions.ResourceNotFoundException;
import com.sistema.notas.mapper.PageMapper;
import com.sistema.notas.mapper.core.EvaluationDetailMapper;
import com.sistema.notas.respository.core.CourseRegistrationRepository;
import com.sistema.notas.respository.core.EvaluationDetailRepository;
import com.sistema.notas.respository.core.EvaluationsRepository;
import com.sistema.notas.respository.core.StudentRepository;
import com.sistema.notas.service.core.EvaluationDetailService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluationDetailServiceImpl implements EvaluationDetailService {

    private final EvaluationDetailRepository evaluationDetailRepository;
    private final EvaluationDetailMapper evaluationDetailMapper;
    private final PageMapper pageMapper;

    // ralaciones
    private final EvaluationsRepository evaluationsRepository;
    private final StudentRepository studentRepository;
    private final CourseRegistrationRepository courseRegistrationRepository;

    @Transactional
    @Override
    public EvaluationDetailResponseDTO save(EvaluationDetailRequestDTO requestDTO) {
        // validar que exista la evaluacion
        Evaluation evaluation = evaluationsRepository.findById(requestDTO.evaluationId())
                .orElseThrow(
                        () -> new BadRequestException("Evaluación no encontrada con ID: " + requestDTO.evaluationId()));

        // validar que la evaluacion este abierta
        if (evaluation.getStatus() == StatusEnum.CLOSED) {
            throw new BadRequestException("No se pueden agregar calificaciones a una evaluación cerrada.");
        }

        // validar que el estudiante exista
        Student student = studentRepository.findById(requestDTO.studentId())
                .orElseThrow(
                        () -> new BadRequestException("Estudiante no encontrado con ID: " + requestDTO.studentId()));

        // validar que no exista una calificacion esa evaluacion y ese estudiante
        if (evaluationDetailRepository.existsByStudentIdAndEvaluationId(requestDTO.studentId(),
                requestDTO.evaluationId())) {
            throw new BadRequestException("El estudiante ya tiene una calificación registrada para esta evaluación.");
        }

        // validar que el estudiante este inscrito en el curso de la evaluacion
        boolean isEnrolled = courseRegistrationRepository.existsByStudentIdAndCourseIdAndStatus(
                student.getId(), evaluation.getCourse().getId(), EnrollmentStatus.ACTIVE);

        if (!isEnrolled) {
            throw new BadRequestException("El estudiante no está inscrito en el curso de la evaluación.");
        }

        EvaluationDetail evaluationDetail = evaluationDetailMapper.toEntity(requestDTO);
        evaluationDetail.setStudent(student);
        evaluationDetail.setEvaluation(evaluation);
        EvaluationDetail savedDetail = evaluationDetailRepository.save(evaluationDetail);
        return evaluationDetailMapper.toResponseDTO(savedDetail);
    }

    @Transactional
    @Override
    public EvaluationDetailResponseDTO update(Integer id, EvaluationDetailEditRequestDTO requestDTO) {

        EvaluationDetail evaluationFind = evaluationDetailRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No existe ningun registro de nota con el id: " + id));

        if (evaluationFind.getEvaluation().getStatus() == StatusEnum.CLOSED) {
            throw new BadRequestException(
                    "No se pueden modificar calificaciones de una evaluación que ya está cerrada.");
        }

        evaluationDetailMapper.updateEntityFromDTO(requestDTO, evaluationFind);

        EvaluationDetail savedDetail = evaluationDetailRepository.save(evaluationFind);

        return evaluationDetailMapper.toResponseDTO(savedDetail);
    }

    @Override
    public void delete(Integer id) {
        EvaluationDetail evaluationFind = evaluationDetailRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No existe ningun registro de nota con el id: " + id));

        evaluationDetailRepository.delete(evaluationFind);
    }

    @Override
    public PaginateResponse<EvaluationDetailResponseDTO> getDetailsPaginated(int page, int size, String search,
            LocalDate fromDate, LocalDate toDate) {

        Pageable pagable = PageRequest.of(page, size);

        Specification<EvaluationDetail> filtros = Specification
                .where(EvaluationDetailSpecification.search(search))
                .and(CatalogoSpecification.<EvaluationDetail>createdBetween(fromDate, toDate));

        Page<EvaluationDetail> evaluations = evaluationDetailRepository.findAll(filtros, pagable);

        return pageMapper.toPaginateResponse(
                evaluations,
                evaluationDetailMapper::toResponseDTO);
    }

    @Override
    public EvaluationDetailFullResponseDTO getOneDetail(Integer id) {
        EvaluationDetail evaluationFind = evaluationDetailRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No existe ningun registro de nota con el id: " + id));
        return evaluationDetailMapper.toFullResponseDTO(evaluationFind);
    }

    @Override
    public EvaluationDetailEditResponseDTO getOneDetailEdit(Integer id) {
        EvaluationDetail evaluationFind = evaluationDetailRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No existe ningun registro de nota con el id: " + id));
        return evaluationDetailMapper.toEditResponseDTO(evaluationFind);
    }

    @Transactional(readOnly = true)
    @Override
    public List<EvaluationDetailSimpleResponseDTO> getGradesByEvaluation(Integer evaluationId) {
        List<EvaluationDetail> evaluationDetails = evaluationDetailRepository.findByEvaluationIdWithStudent(evaluationId);
        return evaluationDetails.stream()
                .map(evaluationDetailMapper::toSimpleResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<EvaluationDetailResponseDTO> getGradesByStudentAndCourse(Integer studentId, Integer courseId) {
        List<EvaluationDetail> evaluationDetails = evaluationDetailRepository.findByStudentAndCourse(studentId, courseId);
        
        return evaluationDetails.stream()
                .map(evaluationDetailMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Double getCurrentStudentAverage(Integer studentId, Integer courseId) {
        return evaluationDetailRepository.calculateCurrentAverageByStudentAndCourse(studentId, courseId);
    }

}
