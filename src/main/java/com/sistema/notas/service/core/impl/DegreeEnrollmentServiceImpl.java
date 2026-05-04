package com.sistema.notas.service.core.impl;

import com.sistema.notas.dto.core.degreeEnrollment.DegreeEnrollmentRequestDTO;
import com.sistema.notas.dto.core.degreeEnrollment.DegreeEnrollmentResponseDTO;
import com.sistema.notas.dto.core.degreeEnrollment.DegreeEnrollmentSimpleResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.entity.core.DegreeEnrollment;
import com.sistema.notas.entity.core.GradeDetail;
import com.sistema.notas.entity.core.Student;
import com.sistema.notas.entity.enums.EnrollmentStatus;
import com.sistema.notas.exceptions.BadRequestException;
import com.sistema.notas.exceptions.ResourceNotFoundException;
import com.sistema.notas.mapper.PageMapper;
import com.sistema.notas.mapper.core.DegreeEnrollmentMapper;
import com.sistema.notas.respository.core.DegreeEnrollmentRepository;
import com.sistema.notas.respository.core.GradeDetailRepository;
import com.sistema.notas.respository.core.StudentRepository;
import com.sistema.notas.service.core.DegreeEnrollmentService;
import com.sistema.notas.specifications.CatalogoSpecification;
import com.sistema.notas.specifications.DegreeEnrollmentSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DegreeEnrollmentServiceImpl implements DegreeEnrollmentService {

    //inyectando dependencias
    private final DegreeEnrollmentRepository degreeEnrollmentRepository;
    private final DegreeEnrollmentMapper degreeEnrollmentMapper;
    private final PageMapper pageMapper;

    //relaciones
    private final StudentRepository studentRepository;
    private final GradeDetailRepository gradeDetailRepository;

    @Override
    public DegreeEnrollmentResponseDTO save(DegreeEnrollmentRequestDTO degreeEnrollmentDTO) {
        //validaciones
        Student student = studentRepository.findById(degreeEnrollmentDTO.studentId())
                .orElseThrow(
                        ()-> new ResourceNotFoundException("No existe ningun Alumno con el ID: " + degreeEnrollmentDTO.studentId())
                );

        GradeDetail gradeDetail = gradeDetailRepository.findById(degreeEnrollmentDTO.gradeDetailId())
                .orElseThrow(
                        ()-> new ResourceNotFoundException("No existe ningun grado con el ID: " + degreeEnrollmentDTO.gradeDetailId())
                );

        Integer targetYear = gradeDetail.getYear();

        if (degreeEnrollmentRepository.hasEnrollmentInYear(student.getId(), targetYear)) {
            throw new BadRequestException("El estudiante ya está matriculado en un grado para el año académico " + targetYear);
        }

        if (degreeEnrollmentRepository.isEnrollmentDuplicated(degreeEnrollmentDTO.gradeDetailId(),
                degreeEnrollmentDTO.studentId())){
            throw new BadRequestException("Ya existe una matricula para este grado y para este estudiante");
        }

        DegreeEnrollment entity = degreeEnrollmentMapper.toEntity(degreeEnrollmentDTO);
        entity.setStudent(student);
        entity.setGradeDetail(gradeDetail);

        DegreeEnrollment degreeEnrollment = degreeEnrollmentRepository.save(entity);
        return degreeEnrollmentMapper.toResponseDTO(degreeEnrollment);
    }

    @Override
    public void delete(Integer id) {
        DegreeEnrollment enrollmentFind = degreeEnrollmentRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("No existe ninguna inscripcion con esta id"));

        degreeEnrollmentRepository.delete(enrollmentFind);
    }

    @Override
    public PaginateResponse<DegreeEnrollmentResponseDTO> obtenerEnrtollmentPaginados(int page, int size, String search, LocalDate fromDate, LocalDate toDate) {
        Pageable pagable = PageRequest.of(page, size);

        Specification<DegreeEnrollment> filtros = Specification
                .where(DegreeEnrollmentSpecification.search(search))
                .and(CatalogoSpecification.<DegreeEnrollment>createdBetween(fromDate, toDate));

        Page<DegreeEnrollment> courses = degreeEnrollmentRepository.findAll(filtros, pagable);

        return pageMapper.toPaginateResponse(
                courses,
                degreeEnrollmentMapper::toResponseDTO);
    }

    @Override
    public List<DegreeEnrollmentSimpleResponseDTO> listarToSelects() {
        List<DegreeEnrollment> enrollments = degreeEnrollmentRepository.findAll();

        return enrollments.stream()
                .map(enr -> new DegreeEnrollmentSimpleResponseDTO(
                        enr.getId(),
                        enr.getStudent().getfullName(),
                        enr.getGradeDetail().getFullName(),
                        enr.getStatus()
                )).toList();
    }

    @Override
    @Transactional
    public DegreeEnrollmentResponseDTO changeStatusEnrollment(Integer id, EnrollmentStatus status) {
        DegreeEnrollment enrollmentFind = degreeEnrollmentRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("No existe ninguna inscripcion con esta id"));

        if (enrollmentFind.getStatus().equals(status)) {
            return degreeEnrollmentMapper.toResponseDTO(enrollmentFind);
        }

        enrollmentFind.setStatus(status);
        return degreeEnrollmentMapper.toResponseDTO(enrollmentFind);
    }
}
