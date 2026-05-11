package com.sistema.notas.service.core.impl;

import java.time.LocalDate;
import java.util.List;

import com.sistema.notas.dto.core.course.*;
import com.sistema.notas.entity.enums.EnrollmentStatus;
import com.sistema.notas.entity.enums.StatusEnum;
import com.sistema.notas.respository.core.EvaluationsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.sistema.notas.respository.core.CourseRegistrationRepository;
import com.sistema.notas.respository.core.CoursesRespository;
import com.sistema.notas.respository.core.GradeDetailRepository;
import com.sistema.notas.respository.core.TeacherRepository;
import com.sistema.notas.service.core.CourseService;
import com.sistema.notas.specifications.CatalogoSpecification;
import com.sistema.notas.specifications.CourseSpecification;

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
    private final CourseRegistrationRepository courseRegistrationRepository;
    // cerrar en cascada
    private final EvaluationsRepository evaluationsRepository;

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
                        () -> new ResourceNotFoundException(
                                "No existe una detalle grado con ID: " + courseDto.gradeDetailId()));

        Course entity = courseMapper.toEntity(courseDto);

        // entity.setStatus(1);//aca hare un enum :C
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

        if (coursesRespository.isCourseDuplicatedForUpdate(courseDto.subjectId(), courseDto.gradeDetailId(),
                courseDto.periodId(), id)) {
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
                        () -> new ResourceNotFoundException(
                                "No existe una detalle grado con ID: " + courseDto.gradeDetailId()));

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
                () -> new ResourceNotFoundException("No existe ningun curso con esta id"));

        coursesRespository.delete(courseFind);
    }

    @Override
    public PaginateResponse<CourseResponseDTO> obtenerCoursePaginados(int page, int size, String search,
            LocalDate fromDate, LocalDate toDate) {
        Pageable pagable = PageRequest.of(page, size);

        Specification<Course> filtros = Specification
                .where(CourseSpecification.search(search))
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
                .map(co -> new CourseSimpleResponseDTO(
                        co.getId(),
                        co.getName(),
                        co.getCode(), co.getStatus(), co.getTotalStudents(),
                        co.getGradeDetail().getYear()))
                .toList();
    }

    @Override
    public CourseFullResponseDTO obtenerOneCourse(Integer id) {
        Course courseFind = coursesRespository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No existe ningun curso con el id: " + id));

        Integer totalStudents = courseRegistrationRepository.countByCourseIdAndStatus(id, EnrollmentStatus.ACTIVE);
        Integer ability = courseFind.getGradeDetail().getAbility() != null ? courseFind.getGradeDetail().getAbility() : 0;
        Integer availableSlots = Math.max(0, ability - totalStudents);

        // 2. Cálculo del porcentaje ya cursado (Evaluaciones en estado CLOSED)
        Double evaluatedPercentage = evaluationsRepository.getEvaluatedPercentage(id, StatusEnum.CLOSED);
        return courseMapper.toFullResponseDTO(courseFind, totalStudents, availableSlots, evaluatedPercentage);
    }

    @Override
    public CourseEditResponseDTO obtenerOneCourseEdit(Integer id) {
        Course courseFind = coursesRespository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No existe ningun curso con el id: " + id));

        return courseMapper.toEditResponseDTO(courseFind);
    }

    @Transactional
    @Override
    public CourseResponseDTO changeCourseStatus(Integer id, StatusEnum newState) {

        Course courseFind = coursesRespository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No existe ningún curso con el id: " + id));

        if (courseFind.getStatus() == newState) {
            return courseMapper.toResponseDTO(courseFind);
        }

        // Cascada: Actualizamos todas las evaluaciones de este curso al nuevo estado
        evaluationsRepository.updateEvaluationStatusByCourseId(id, newState);

        courseFind.setStatus(newState);
        coursesRespository.save(courseFind);
        return courseMapper.toResponseDTO(courseFind);
    }

    @Override
    public AvailablePercentageResponseDTO obtenerAvailablePercentage(Integer id) {
        if (!coursesRespository.existsById(id)) {
            throw new ResourceNotFoundException("No existe el curso con ID: " + id);
        }

        Double currentAccumulated = evaluationsRepository.getAccumulatedPercentage(id, null);
        double remaining = 100.0 - currentAccumulated;
        remaining = Math.max(0.0, remaining);

        return new AvailablePercentageResponseDTO(remaining);
    }

}
