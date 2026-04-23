package com.sistema.notas.service.core.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistema.notas.dto.core.course.CourseFullResponseDTO;
import com.sistema.notas.dto.core.course.CourseRequestDTO;
import com.sistema.notas.dto.core.course.CourseResponseDTO;
import com.sistema.notas.dto.core.course.CourseSimpleResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.entity.catalogues.Period;
import com.sistema.notas.entity.catalogues.Subject;
import com.sistema.notas.entity.core.Course;
import com.sistema.notas.entity.core.GradeDetail;
import com.sistema.notas.entity.core.Teacher;
import com.sistema.notas.exceptions.BadRequestException;
import com.sistema.notas.exceptions.ResourceNotFoundException;
import com.sistema.notas.mapper.PageMapper;
import com.sistema.notas.mapper.core.CourseMapper;
import com.sistema.notas.respository.catalogues.PeriodRespository;
import com.sistema.notas.respository.catalogues.SubjectRepository;
import com.sistema.notas.respository.core.CoursesRespository;
import com.sistema.notas.respository.core.GradeDetailRepository;
import com.sistema.notas.respository.core.TeacherRepository;
import com.sistema.notas.service.core.CourseService;
import com.sistema.notas.specifications.CatalogoSpecification;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    // tools para todo el proceso
    private final CoursesRespository coursesRespository;
    private final CourseMapper courseMapper;
    private final PageMapper pageMapper;

    // relaciones
    private final TeacherRepository teacherRepository;
    private final PeriodRespository periodRespository;
    private final SubjectRepository subjectRepository;
    private final GradeDetailRepository gradeDetailRepository;

    @Override
    public CourseResponseDTO save(CourseRequestDTO courseDto) {
        if (coursesRespository.isCourseDuplicated(courseDto.subjectId(), courseDto.gradeDetailId(),
                courseDto.periodId())) {
            throw new BadRequestException("Ya existe un curso con los detalles especificados");
        }

        // validaciones
        Teacher teacher = teacherRepository.findById(courseDto.teacherId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("No existe el maestro con ID: " + courseDto.teacherId()));

        Period period = periodRespository.findById(courseDto.periodId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("No existe un periodo con el ID: " + courseDto.periodId()));

        Subject subject = subjectRepository.findById(courseDto.subjectId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("No existe una materia con ID: " + courseDto.subjectId()));

        GradeDetail gradeDetail = gradeDetailRepository.findById(courseDto.gradeDetailId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("No existe una detalle grado con ID: " + courseDto.gradeDetailId()));

        Course entity = courseMapper.toEntity(courseDto);

        entity.setStatus(1);//aca hare un enum :C
        entity.setSubject(subject);
        entity.setTeacher(teacher);
        entity.setPeriod(period);
        entity.setGradeDetail(gradeDetail);

        Course saved = coursesRespository.save(entity);
        return courseMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public CourseResponseDTO update(Integer id, CourseRequestDTO courseDto) {
        Course courseFind = coursesRespository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("No existe ningun curso con el id: " + id));

        if (coursesRespository.isCourseDuplicatedForUpdate(courseDto.subjectId(), courseDto.gradeDetailId(), courseDto.periodId(), id)) {
            throw new BadRequestException("Ya existe un courso con estos datos.");
        }

        // validaciones
        Teacher teacher = teacherRepository.findById(courseDto.teacherId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("No existe el maestro con ID: " + courseDto.teacherId()));

        Period period = periodRespository.findById(courseDto.periodId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("No existe un periodo con el ID: " + courseDto.periodId()));

        Subject subject = subjectRepository.findById(courseDto.subjectId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("No existe una materia con ID: " + courseDto.subjectId()));

        GradeDetail gradeDetail = gradeDetailRepository.findById(courseDto.gradeDetailId())
                .orElseThrow(
                        () -> new ResourceNotFoundException("No existe una detalle grado con ID: " + courseDto.gradeDetailId()));

        courseFind.setSubject(subject);
        courseFind.setTeacher(teacher);
        courseFind.setPeriod(period);
        courseFind.setGradeDetail(gradeDetail);


        courseMapper.updateEntityFromDTO(courseDto, courseFind);
        return courseMapper.toResponseDTO(courseFind);
    }

    @Override
    public void delete(Integer id) {
        Course courseFind = coursesRespository.findById(id).orElseThrow(
            ()->new ResourceNotFoundException("No existe ningun curso con esta id"));

        coursesRespository.delete(courseFind);
    }

    @Override
    public PaginateResponse<CourseResponseDTO> obtenerCoursePaginados(int page, int size, String search,
            LocalDate fromDate, LocalDate toDate) {
        Pageable pagable = PageRequest.of(page, size);

        //cambiar los specifications
        Specification<Course> filtros = Specification
                .where(CatalogoSpecification.<Course>searchContains(search))
                .and(CatalogoSpecification.<Course>createdBetween(fromDate, toDate));

        Page<Course> courses = coursesRespository.findAll(filtros, pagable);

        return pageMapper.toPaginateResponse(
                courses,
                courseMapper::toResponseDTO);
    }

    @Override
    public List<CourseSimpleResponseDTO> listarToSelects() {
        List<Course> courses = coursesRespository.findAll();

        return courses.stream()
            .map(co-> new CourseSimpleResponseDTO(
                co.getId(),
                co.getName(),
                co.getCode(),
                co.getGradeDetail().getYear(),
                co.getStatus()
            )).toList();
    }

    @Override
    public CourseFullResponseDTO obtenerOneCourse(Integer id) {
        Course courseFind = coursesRespository.findById(id).orElseThrow(
            () -> new ResourceNotFoundException("No existe ningun curso con el id: " + id));

        return courseMapper.toFullResponseDTO(courseFind);
    }

}
