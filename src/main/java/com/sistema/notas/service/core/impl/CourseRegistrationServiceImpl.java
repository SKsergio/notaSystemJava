package com.sistema.notas.service.core.impl;

import java.time.LocalDate;
import java.util.List;

import com.sistema.notas.dto.core.courseRegistration.BatchRegistrationCourseDTO;
import com.sistema.notas.exceptions.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistema.notas.dto.core.courseRegistration.CourseRegistrationRequestDTO;
import com.sistema.notas.dto.core.courseRegistration.CourseRegistrationResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.entity.core.Course;
import com.sistema.notas.entity.core.CourseRegistration;
import com.sistema.notas.entity.core.Student;
import com.sistema.notas.entity.enums.EnrollmentStatus;
import com.sistema.notas.exceptions.ResourceNotFoundException;
import com.sistema.notas.mapper.PageMapper;
import com.sistema.notas.mapper.core.CourseRegistrationMapper;
import com.sistema.notas.respository.core.CourseRegistrationRepository;
import com.sistema.notas.respository.core.CoursesRespository;
import com.sistema.notas.respository.core.StudentRepository;
import com.sistema.notas.service.core.CourseRegistrationService;
import com.sistema.notas.specifications.CatalogoSpecification;
import com.sistema.notas.specifications.CourseRegistrationSpecification;
import org.springframework.data.domain.Pageable;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CourseRegistrationServiceImpl implements CourseRegistrationService {

    private final CourseRegistrationRepository courseRegistrationRepository;
    private final CourseRegistrationMapper courseRegistrationMapper;
    private final PageMapper pageMapper;

    // relaciones
    private final StudentRepository studentRepository;
    private final CoursesRespository coursesRespository;

    @Override
    public CourseRegistrationResponseDTO save(CourseRegistrationRequestDTO requestDTO) {
        Student student = studentRepository.findById(requestDTO.studentId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "No existe ningun Alumno con el ID: " + requestDTO.studentId()));

        Course course = coursesRespository.findById(requestDTO.courseId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "No existe ningun curso con el ID: " + requestDTO.courseId()));

        if (courseRegistrationRepository.isEnrollmentDuplicated(requestDTO.courseId(), requestDTO.studentId())) {
            throw new IllegalArgumentException("Ya existe una inscripción para este estudiante y curso");
        }

        CourseRegistration entity = courseRegistrationMapper.toEntity(requestDTO);
        entity.setStudent(student);
        entity.setCourse(course);
        CourseRegistration savedEntity = courseRegistrationRepository.save(entity);
        return courseRegistrationMapper.toResponseDTO(savedEntity);
    }

    @Override
    public List<CourseRegistrationResponseDTO> enrollInBatch(BatchRegistrationCourseDTO requestDTO) {
        Course course = coursesRespository.findById(requestDTO.courseId())
            .orElseThrow(
                () -> new ResourceNotFoundException("No existe ningun curso con el ID: " + requestDTO.courseId()));


        //validar que no excedean de la cantidad cupos disponibles para el grado.
        List<Student> studentsToEnroll = studentRepository.findAllById(requestDTO.studentIds());

        if (studentsToEnroll.size() != requestDTO.studentIds().size()) {
            throw new BadRequestException("Algunos ids proporcionados no existen");
        }

        List<Integer> duplicatedInCourse = courseRegistrationRepository.findDuplicatedStudentIdsInCourse(
                course.getId(), requestDTO.studentIds()
        );

        if (!duplicatedInCourse.isEmpty()) {
            throw new BadRequestException("Los siguientes estudiantes ya están matriculados en esta curso: IDs " + duplicatedInCourse);
        }

        List<CourseRegistration> newRegistration = studentsToEnroll.stream().map( student -> {
            CourseRegistration registration = new CourseRegistration();
            registration.setStudent(student);
            registration.setCourse(course);
            return registration;
        }).toList();
        List<CourseRegistration> savedRegistration = courseRegistrationRepository.saveAll(newRegistration);
        return savedRegistration.stream()
                .map(courseRegistration -> )
    }

    @Override
    public void delete(Integer id) {
        CourseRegistration registrationFind = courseRegistrationRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("No existe ninguna inscripcion con esta id"));

        courseRegistrationRepository.delete(registrationFind);
    }

    @Override
    public PaginateResponse<CourseRegistrationResponseDTO> obtenerRegistrosPaginados(int page, int size,
            String search, LocalDate fromDate, LocalDate toDate) {

        Pageable pagable = PageRequest.of(page, size);

        Specification<CourseRegistration> filtros = Specification
                .where(CourseRegistrationSpecification.search(search))
                .and(CatalogoSpecification.<CourseRegistration>createdBetween(fromDate, toDate));

        Page<CourseRegistration> courses = courseRegistrationRepository.findAll(filtros, pagable);

        return pageMapper.toPaginateResponse(
                courses,
                courseRegistrationMapper::toResponseDTO);
    }

    @Override
    public List<CourseRegistrationResponseDTO> listarToSelects() {
       List<CourseRegistration> registrations = courseRegistrationRepository.findAll();
       return courseRegistrationMapper.toResponseDTOList(registrations);
    }

    @Override
    public CourseRegistrationResponseDTO changeStatusRegistration(Integer id, EnrollmentStatus status) {
         CourseRegistration registrationFind = courseRegistrationRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("No existe ninguna inscripcion con esta id"));

        if (registrationFind.getStatus().equals(status)) {
            return courseRegistrationMapper.toResponseDTO(registrationFind);
        }

        registrationFind.setStatus(status);
        return courseRegistrationMapper.toResponseDTO(registrationFind);
    }

    @Transactional(readOnly = true)
    @Override
    public PaginateResponse<CourseRegistrationResponseDTO> getRegistrationByCourse(int page, int size,Integer CourseId) {
        Pageable pagable = PageRequest.of(page, size);
        Page<CourseRegistration> courseRegistration = courseRegistrationRepository.findByCourseId(CourseId, pagable);

        return pageMapper.toPaginateResponse(
                courseRegistration,
                courseRegistrationMapper::toResponseDTO);
    }

}
