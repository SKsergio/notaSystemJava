package com.sistema.notas.service.core.impl;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import com.sistema.notas.entity.core.DegreeEnrollment;
import com.sistema.notas.entity.core.GradeDetail;
import com.sistema.notas.entity.enums.EnrollmentStatus;
import com.sistema.notas.respository.core.DegreeEnrollmentRepository;
import com.sistema.notas.specifications.core.people.PersonSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.sistema.notas.dto.core.student.StudentEditRequestDTO;
import com.sistema.notas.dto.core.student.StudentFullResponseDTO;
import com.sistema.notas.dto.core.student.StudentRequestDTO;
import com.sistema.notas.dto.core.student.StudentResponseDTO;
import com.sistema.notas.dto.core.student.StudentResponseEditDTO;
import com.sistema.notas.dto.core.student.StudentSimpleResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.entity.core.Student;
import com.sistema.notas.exceptions.BadRequestException;
import com.sistema.notas.mapper.PageMapper;
import com.sistema.notas.mapper.core.StudentMapper;
import com.sistema.notas.respository.core.StudentRepository;
import com.sistema.notas.service.core.StudentService;
import com.sistema.notas.service.fileStorage.FileStorageService;
import com.sistema.notas.specifications.catalogue.CatalogoSpecification;
import com.sistema.notas.specifications.core.people.StudentSpecification;
import com.sistema.notas.entity.security.User;
import com.sistema.notas.entity.enums.Role;
import com.sistema.notas.respository.security.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final PageMapper pageMapper;
    // servicio de imagenes
    private final FileStorageService fileStorageService;
    private final DegreeEnrollmentRepository degreeEnrollmentRepository;

    @Override
    public StudentResponseDTO save(StudentRequestDTO studentRequestDTO) {
        if (studentRepository.existsByEmail(studentRequestDTO.email())) {
            throw new BadRequestException(
                    "Ya hay un estudiante Registrado con el correo: " + studentRequestDTO.email());
        }

        Student entity = studentMapper.toEntity(studentRequestDTO);
        if (studentRequestDTO.photo() != null && !studentRequestDTO.photo().isEmpty()) {
            String fileName = fileStorageService.storeFile(studentRequestDTO.photo());
            entity.setRoutePhoto(fileName);
        }

        Student saved = studentRepository.save(entity);
        if (!userRepository.existsByEmail(saved.getEmail())) {

            User user = new User();
            user.setEmail(saved.getEmail());
            user.setPasswordHash(
                    passwordEncoder.encode("123456")
            );
            user.setRole(Role.STUDENT);
            user.setStudentId(saved.getId());
            user.setFirstLogin(true);
            userRepository.save(user);
        }
        return studentMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public StudentResponseDTO update(Integer id, StudentEditRequestDTO studentRequestDTO) {
        Student studentFind = studentRepository.findById(id).orElseThrow(
                () -> new BadRequestException("No se encontró el estudiante con id: " + id));

        if (studentRepository.existsByEmailAndIdNot(studentRequestDTO.email(), id)) {
            throw new BadRequestException(
                    "Ya hay un estudiante Registrado con el correo: " + studentRequestDTO.email());
        }

        studentMapper.updateEntityFromDTO(studentRequestDTO, studentFind);
        if (studentRequestDTO.photo() != null && !studentRequestDTO.photo().isEmpty()) {
            // obtener nombre de la antigua
            String oldNamePhoto = studentFind.getRoutePhoto();

            // nueva foto
            String fileName = fileStorageService.storeFile(studentRequestDTO.photo());
            studentFind.setRoutePhoto(fileName);

            if (oldNamePhoto != null && !oldNamePhoto.isEmpty()) {
                fileStorageService.delteFile(oldNamePhoto);
            }
        }
        return studentMapper.toResponseDTO(studentFind);
    }

    @Override
    public void delete(Integer id) {
        Student studentFind = studentRepository.findById(id).orElseThrow(
                () -> new BadRequestException("No se encontró el estudiante con id: " + id));
        studentRepository.delete(studentFind);
    }

    @Override
    public PaginateResponse<StudentResponseDTO> obtenerStudentPaginados(int page, int size, String search, LocalDate startDate, LocalDate endDate) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<Student> filtros = Specification
                .where(StudentSpecification.searchAll(search))
                .and(CatalogoSpecification.<Student>createdBetween(startDate, endDate));

        Page<Student> studentsPage = studentRepository.findAll(filtros, pageable);

        Map<Integer, String> studentDegreeMap = getActiveDegreesMapForStudents(studentsPage.getContent());

        Page<StudentResponseDTO> dtoPage = studentsPage.map(student -> {
            String grado = studentDegreeMap.getOrDefault(student.getId(), "No matriculado");
            return studentMapper.toResponseDTOWithDegree(student, grado);
        });

        return pageMapper.toPaginateResponse(dtoPage, dto -> dto);
    }


    // Método privado de apoyo dentro de StudentService
    private Map<Integer, String> getActiveDegreesMapForStudents(List<Student> students) {
        List<Integer> studentIds = students.stream()
                .map(Student::getId)
                .toList();

        if (studentIds.isEmpty()) {
            return Collections.emptyMap();
        }

        int currentYear = LocalDate.now().getYear();

        List<DegreeEnrollment> activeEnrollments = degreeEnrollmentRepository
                .findActiveEnrollmentsByStudentIdsAndYear(studentIds, currentYear, EnrollmentStatus.ACTIVE);

        return activeEnrollments.stream()
                .collect(Collectors.toMap(
                        en -> en.getStudent().getId(),
                        en -> en.getGradeDetail().getFullName(),
                        (existing, replacement) -> existing // Por si un alumno tiene 2 matrículas activas por error, no crashea
                ));
    }

    @Override
    public List<StudentSimpleResponseDTO> listartoSelect(String search) {
        Specification<Student> filtros = Specification
                .where(StudentSpecification.searchAll(search));

        List<Student> students = studentRepository.findAll(filtros);

        return students.stream()
                .map(studentMapper::toSimpleResponseDTO)
                .toList();
    }

    @Override
    public StudentFullResponseDTO obtenerStudent(Integer id) {
        Student studentFind = studentRepository.findById(id).orElseThrow(
                () -> new BadRequestException("No se encontró el estudiante con id: " + id));

        int currentYear = LocalDate.now().getYear();

        // Buscamos el detalle del grado
        Optional<GradeDetail> gradeDetailOpt = degreeEnrollmentRepository.findCurrentGradeDetail(
                studentFind.getId(), currentYear, EnrollmentStatus.ACTIVE);

        String degreeName = gradeDetailOpt.map(GradeDetail::getFullName).orElse("No matriculado");
        Integer gradeDetailId = gradeDetailOpt.map(GradeDetail::getId).orElse(null);

        return studentMapper.toFullResponse(studentFind, degreeName, gradeDetailId);
    }

    @Override
    public StudentResponseEditDTO obtenerStudentEdit(Integer id) {
        Student studentFind = studentRepository.findById(id).orElseThrow(
                () -> new BadRequestException("No se encontró el estudiante con id: " + id));

        return studentMapper.toResponseEditDto(studentFind);
    }

}
