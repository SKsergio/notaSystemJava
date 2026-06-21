package com.sistema.notas.service.core.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.sistema.notas.dto.core.evaluationDetail.StudentGradeDTO;
import com.sistema.notas.dto.core.evaluationDetail.*;
import com.sistema.notas.entity.core.CourseRegistration;
import com.sistema.notas.specifications.catalogue.CatalogoSpecification;
import com.sistema.notas.specifications.core.evaluation.EvaluationDetailSpecification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    @Override
    public List<EvaluationDetailResponseDTO> calificateinBatch(BatchEvaluationDetailDTO requestDTO) {

        Evaluation evaluation = evaluationsRepository.findById(requestDTO.evaluationId())
                .orElseThrow(() -> new ResourceNotFoundException("Evaluación no encontrada con ID: " + requestDTO.evaluationId()));

        if (evaluation.getStatus() == StatusEnum.CLOSED) {
            throw new BadRequestException("No se pueden modificar calificaciones en una evaluación cerrada.");
        }

        //(Nuevos vs Existentes)
        List<StudentGradeDTO> newGrades = requestDTO.grades().stream()
                .filter(g -> g.evaluationDetailId() == null).toList();

        List<StudentGradeDTO> existingGrades = requestDTO.grades().stream()
                .filter(g -> g.evaluationDetailId() != null).toList();

        List<EvaluationDetail> finalDetailsToSave = new ArrayList<>();

        //  PROCESAR LOS NUEVOS (INSERTS)
        if (!newGrades.isEmpty()) {
            List<Integer> newStudentIds = newGrades.stream().map(StudentGradeDTO::studentId).toList();

            // A. Validar que los alumnos existan
            List<Student> studentsToEval = studentRepository.findAllById(newStudentIds);
            if (studentsToEval.size() != newStudentIds.size()) {
                throw new BadRequestException("Algunos alumnos nuevos no existen en el sistema.");
            }

            // B. Validar inscripción al curso
            List<Integer> enrolledIds = courseRegistrationRepository.findEnrolledStudentIds(
                    evaluation.getCourse().getId(), newStudentIds, EnrollmentStatus.ACTIVE);
            if (enrolledIds.size() != newStudentIds.size()) {
                throw new BadRequestException("Hay alumnos en la lista que no están inscritos en el curso.");
            }

            // C. Validar duplicados (por si acaso alguien manda un null cuando ya existía)
            List<Integer> duplicatedIds = evaluationDetailRepository.findDuplicatedStudentIdsInEvaluation(
                    evaluation.getId(), newStudentIds);
            if (!duplicatedIds.isEmpty()) {
                throw new BadRequestException("Intento de crear nota duplicada para los alumnos: " + duplicatedIds);
            }

            // D. Armar las entidades nuevas
            List<EvaluationDetail> newDetails = newGrades.stream().map(gradeInfo -> {
                Student student = studentsToEval.stream()
                        .filter(s -> s.getId().equals(gradeInfo.studentId())).findFirst().get();
                EvaluationDetail detail = new EvaluationDetail();
                detail.setEvaluation(evaluation);
                detail.setStudent(student);
                detail.setGrade(gradeInfo.grade());
                detail.setFeedback(gradeInfo.feedback());
                return detail;
            }).toList();

            finalDetailsToSave.addAll(newDetails);
        }

        // PROCESAR LOS EXISTENTES (UPDATES)
        if (!existingGrades.isEmpty()) {
            List<Integer> existingDetailIds = existingGrades.stream().map(StudentGradeDTO::evaluationDetailId).toList();

            List<EvaluationDetail> detailsToUpdate = evaluationDetailRepository.findAllById(existingDetailIds);

            for (EvaluationDetail detail : detailsToUpdate) {
                StudentGradeDTO updateData = existingGrades.stream()
                        .filter(g -> g.evaluationDetailId().equals(detail.getId())).findFirst().get();

                detail.setGrade(updateData.grade());
                detail.setFeedback(updateData.feedback());
                finalDetailsToSave.add(detail);
            }
        }
        List<EvaluationDetail> savedDetails = evaluationDetailRepository.saveAll(finalDetailsToSave);

        return savedDetails.stream()
                .map(evaluationDetailMapper::toResponseDTO)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public PaginateResponse<EvaluationGradebookDTO> getEvaluationGradebook(int page, int size, Integer evaluationId) {
        Pageable pageable = PageRequest.of(page, size);

        Evaluation evaluation = evaluationsRepository.findById(evaluationId)
                .orElseThrow(() -> new ResourceNotFoundException("Evaluación no encontrada"));

        Page<CourseRegistration> enrollmentsPage = courseRegistrationRepository
                .findByCourseIdAndStatus(evaluation.getCourse().getId(), EnrollmentStatus.ACTIVE, pageable);

        List<EvaluationDetail> existingGrades = evaluationDetailRepository
                .findByEvaluationId(evaluationId);

        Page<EvaluationGradebookDTO> dtoPage = enrollmentsPage.map(enrollment -> {
            Optional<EvaluationDetail> studentGrade = existingGrades.stream()
                    .filter(grade -> grade.getStudent().getId().equals(enrollment.getStudent().getId()))
                    .findFirst();

            return new EvaluationGradebookDTO(
                    enrollment.getStudent().getId(),
                    enrollment.getStudent().getfullName(),
                    enrollment.getStudent().getCarnet(),
                    studentGrade.map(EvaluationDetail::getId).orElse(null),
                    studentGrade.map(EvaluationDetail::getGrade).orElse(null),
                    studentGrade.map(EvaluationDetail::getFeedback).orElse(null)
            );
        });

        // Asumiendo que usas tu mapper genérico:
        return pageMapper.toPaginateResponse(dtoPage, dto -> dto);
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
