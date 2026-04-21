package com.sistema.notas.service.core.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.sistema.notas.dto.core.gradeDetail.GradeDetailFullResponseDTO;
import com.sistema.notas.dto.core.gradeDetail.GradeDetailRequestDTO;
import com.sistema.notas.dto.core.gradeDetail.GradeDetailResponseDTO;
import com.sistema.notas.dto.core.gradeDetail.GradeDetailSimpleResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.entity.catalogues.Degree;
import com.sistema.notas.entity.catalogues.Section;
import com.sistema.notas.entity.core.GradeDetail;
import com.sistema.notas.entity.core.Teacher;
import com.sistema.notas.exceptions.BadRequestException;
import com.sistema.notas.exceptions.ResourceNotFoundException;
import com.sistema.notas.mapper.PageMapper;
import com.sistema.notas.mapper.core.GradeDetailMapper;
import com.sistema.notas.respository.catalogues.DegreeRespository;
import com.sistema.notas.respository.catalogues.SectionRespository;
import com.sistema.notas.respository.core.GradeDetailRepository;
import com.sistema.notas.respository.core.TeacherRepository;
import com.sistema.notas.service.core.GradeDetailService;
import com.sistema.notas.specifications.CatalogoSpecification;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GradeDetailServiceImpl implements GradeDetailService {

    private final GradeDetailRepository gradeDetailRepository;
    private final GradeDetailMapper gradeDetailMapper;
    private final PageMapper pageMapper;
    // realciones
    private final DegreeRespository degreeRespository;
    private final SectionRespository sectionRespository;
    private final TeacherRepository teacherRepository;

    @Override
    public GradeDetailResponseDTO save(GradeDetailRequestDTO gradeDetail) {
        if (gradeDetailRepository.existsByDegreeIdAndSectionIdAndYear(gradeDetail.degreeId(), gradeDetail.sectionId(),
                gradeDetail.year())) {
            throw new BadRequestException("Ya existe un detalle de grado con esa seccion y grado");
        }

        Degree degree = degreeRespository.findById(gradeDetail.degreeId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("No existe el grado con ID: " + gradeDetail.degreeId()));

        Section section = sectionRespository.findById(gradeDetail.sectionId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("No existe la sección con ID: " + gradeDetail.sectionId()));

        Teacher tutor = teacherRepository.findById(gradeDetail.tutorId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("No existe el maestro con ID: " + gradeDetail.tutorId()));

        GradeDetail entity = gradeDetailMapper.toEntity(gradeDetail);

        entity.setDegree(degree);
        entity.setTutor(tutor);
        entity.setSection(section);

        GradeDetail saved = gradeDetailRepository.save(entity);
        return gradeDetailMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public GradeDetailResponseDTO update(Integer id, GradeDetailRequestDTO gradeDetail) {
        GradeDetail gradeDetailFind = gradeDetailRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No existe ningun detalle de grado con el id: " + id));

        if (gradeDetailRepository.existsByDegreeIdAndSectionIdAndYearAndIdNot(
                gradeDetail.degreeId(),
                gradeDetail.sectionId(),
                gradeDetail.year(),
                id)) {
            throw new BadRequestException("Ya existe un detalle de grado para esa sección en este año.");
        }

        Degree degree = degreeRespository.findById(gradeDetail.degreeId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("No existe el grado con ID: " + gradeDetail.degreeId()));

        Section section = sectionRespository.findById(gradeDetail.sectionId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("No existe la sección con ID: " + gradeDetail.sectionId()));

        Teacher tutor = teacherRepository.findById(gradeDetail.tutorId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("No existe el maestro con ID: " + gradeDetail.tutorId()));

        gradeDetailFind.setDegree(degree);
        gradeDetailFind.setSection(section);
        gradeDetailFind.setTutor(tutor);

        gradeDetailMapper.updateEntityFromDTO(gradeDetail, gradeDetailFind);
        return gradeDetailMapper.toResponse(gradeDetailFind);
    }

    @Override
    public void delete(Integer id) {
        GradeDetail gradeDetailFind = gradeDetailRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No existe ningun detalle de grado con el id: " + id));

        gradeDetailRepository.delete(gradeDetailFind);
    }

    @Override
    public PaginateResponse<GradeDetailResponseDTO> obtenerGradaDetailPaginados(int page, int size, String search,
            LocalDate fromDate, LocalDate toDate) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<GradeDetail> filtros = Specification
                .where(CatalogoSpecification.<GradeDetail>searchContains(search))
                .and(CatalogoSpecification.<GradeDetail>createdBetween(fromDate, toDate));

        Page<GradeDetail> gradesDetail = gradeDetailRepository.findAll(filtros, pageable);

        return pageMapper.toPaginateResponse(
                gradesDetail,
                gradeDetailMapper::toResponse);
    }

    @Override
    public List<GradeDetailSimpleResponseDTO> listarToSelects() {
        List<GradeDetail> gradeDetails = gradeDetailRepository.findAll();

        return gradeDetails.stream()
                .map(gdt -> new GradeDetailSimpleResponseDTO(
                        gdt.getId(),
                        gdt.getSection().getName(),
                        gdt.getDegree().getName(),
                        gdt.getTutor().getFirstName()))
                .toList();
    }

    @Override
    public GradeDetailFullResponseDTO obtenerOneGradeDetail(Integer id) {
        GradeDetail gradeDetailFind = gradeDetailRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No existe ningun detalle de grado con el id: " + id));

        return gradeDetailMapper.toFullResponseDTO(gradeDetailFind);
    }

}
