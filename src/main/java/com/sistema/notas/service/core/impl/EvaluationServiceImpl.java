package com.sistema.notas.service.core.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.sistema.notas.dto.core.evaluations.EvaluationFullResponseDTO;
import com.sistema.notas.dto.core.evaluations.EvaluationRequestDTO;
import com.sistema.notas.dto.core.evaluations.EvaluationSimpleResponse;
import com.sistema.notas.dto.core.evaluations.EvaluationsResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.entity.core.Course;
import com.sistema.notas.entity.core.Evaluation;
import com.sistema.notas.exceptions.BadRequestException;
import com.sistema.notas.exceptions.ResourceNotFoundException;
import com.sistema.notas.mapper.PageMapper;
import com.sistema.notas.mapper.core.EvaluationsMapper;
import com.sistema.notas.respository.core.CoursesRespository;
import com.sistema.notas.respository.core.EvaluationsRepository;
import com.sistema.notas.service.core.EvaluationService;
import com.sistema.notas.specifications.CatalogoSpecification;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EvaluationServiceImpl implements EvaluationService {

    private final EvaluationsRepository evaluationsRepository;
    private final EvaluationsMapper evaluationsMapper;
    private final PageMapper pageMapper;

    // relaciones
    private final CoursesRespository coursesRespository;

    @Override
    public EvaluationsResponseDTO save(EvaluationRequestDTO evaluationDTO) {
        if (evaluationDTO.startDate().isAfter(evaluationDTO.endDate())) {
            throw new BadRequestException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }

        Course course = coursesRespository.findById(evaluationDTO.courseId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("No existe el curso con ID: " + evaluationDTO.courseId()));

        Evaluation evaluation = evaluationsMapper.toEntity(evaluationDTO);

        evaluation.setCourse(course);
        evaluation.setStatus(1);

        Evaluation saved = evaluationsRepository.save(evaluation);
        return evaluationsMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public EvaluationsResponseDTO update(Integer id, EvaluationRequestDTO evaluationDTO) {
        Evaluation evaluationFind = evaluationsRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No existe ninguna evaluación con el id: " + id));

        if (evaluationDTO.startDate().isAfter(evaluationDTO.endDate())) {
            throw new BadRequestException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }

        Course course = coursesRespository.findById(evaluationDTO.courseId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("No existe el curso con ID: " + evaluationDTO.courseId()));

        evaluationFind.setCourse(course);
        evaluationsMapper.updateEntityFromDTO(evaluationDTO, evaluationFind);
        return evaluationsMapper.toResponseDTO(evaluationFind);
    }

    @Override
    public void delete(Integer id) {
        Evaluation evaluationFind = evaluationsRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No existe ninguna evaluación con el id: " + id));

        evaluationsRepository.delete(evaluationFind);
    }

    @Override
    public PaginateResponse<EvaluationsResponseDTO> obtenerEvaluations(int page, int size, String search,
            LocalDate fromDate, LocalDate toDate) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<Evaluation> filtros = Specification
                .where(CatalogoSpecification.<Evaluation>searchContains(search))
                .and(CatalogoSpecification.<Evaluation>createdBetween(fromDate, toDate));

        Page<Evaluation> evaluations = evaluationsRepository.findAll(filtros, pageable);

        return pageMapper.toPaginateResponse(
                evaluations,
                evaluationsMapper::toResponseDTO);
    }

    @Override
    public List<EvaluationSimpleResponse> listarToSelects() {
        List<Evaluation> evaluations = evaluationsRepository.findAll();

        return evaluations.stream()
                .map(ev -> new EvaluationSimpleResponse(
                        ev.getId(),
                        ev.getName(),
                        ev.getStatus(),
                        ev.getCourse().getName(),
                        ev.getDaysRemaning()))
                .toList();
    }

    @Override
    public EvaluationFullResponseDTO obtenerOneEvaluation(Integer id) {
         Evaluation evaluationFind = evaluationsRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No existe ninguna evaluación con el id: " + id));
        return evaluationsMapper.toFullResponseDTO(evaluationFind);
    }
    
    @Override
    public EvaluationsResponseDTO openEvaluation(Integer id) {
        Evaluation evaluationFind = evaluationsRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No existe ninguna evaluación con el id: " + id));

        evaluationFind.setStatus(2);//abierto
        return evaluationsMapper.toResponseDTO(evaluationFind);
    }

    @Override
    public EvaluationsResponseDTO closeEvaluation(Integer id) {
         Evaluation evaluationFind = evaluationsRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No existe ninguna evaluación con el id: " + id));

        evaluationFind.setStatus(3);//CERRADO
        return evaluationsMapper.toResponseDTO(evaluationFind);
    }

}
