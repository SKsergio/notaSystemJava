package com.sistema.notas.service.core.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.sistema.notas.entity.core.DegreeEnrollment;
import com.sistema.notas.entity.core.GradeDetail;
import com.sistema.notas.entity.enums.EnrollmentStatus;
import com.sistema.notas.respository.core.DegreeEnrollmentRepository;
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
import com.sistema.notas.specifications.CatalogoSpecification;
import com.sistema.notas.specifications.StudentSpecification;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

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
    public PaginateResponse<StudentResponseDTO> obtenerStudentPaginados(int page, int size, String search,
                                                                        LocalDate startDate, LocalDate endDate) {

        Pageable pagable = PageRequest.of(page, size);

        Specification<Student> filtros = Specification
                .where(StudentSpecification.search(search))
                .and(CatalogoSpecification.<Student>createdBetween(startDate, endDate));

        // 1. Consulta principal (1 Query)
        Page<Student> students = studentRepository.findAll(filtros, pagable);

        // 2. Extraemos los IDs de los alumnos de esta página específica (Máximo 10 o 20 IDs)
        List<Integer> studentIds = students.getContent().stream()
                .map(Student::getId)
                .toList();

        int currentYear = LocalDate.now().getYear(); // Te dará 2026 dinámicamente
        List<DegreeEnrollment> activeEnrollments = new ArrayList<>();

        // 3. Segunda Consulta en lote (1 Query extra, evita el N+1)
        // Validamos que la lista no esté vacía para que la cláusula IN de SQL no explote
        if (!studentIds.isEmpty()) {
            activeEnrollments = degreeEnrollmentRepository.findActiveEnrollmentsByStudentIdsAndYear(
                    studentIds, currentYear, EnrollmentStatus.ACTIVE);
        }

        // 4. Creamos un diccionario (Map) para búsquedas ultrarrápidas O(1) en memoria
        // Llave: ID del estudiante | Valor: Nombre completo del grado
        Map<Integer, String> studentDegreeMap = activeEnrollments.stream()
                .collect(Collectors.toMap(
                        en -> en.getStudent().getId(),
                        en -> en.getGradeDetail().getFullName()
                ));

        // 5. Transformamos cada Student de la base de datos a su DTO inyectando el grado
        Page<StudentResponseDTO> dtoPage = students.map(student -> {
            // Sacamos el grado del mapa (o el default)
            String grado = studentDegreeMap.getOrDefault(student.getId(), "No matriculado");

            // ¡MapStruct hace la magia y crea el record inmutable con el grado incluido!
            return studentMapper.toResponseDTOWithDegree(student, grado);
        });

        // 6. Usamos tu pageMapper pasando un mapper de identidad (dto -> dto)
        return pageMapper.toPaginateResponse(dtoPage, dto -> dto);
    }

    @Override
    public List<StudentSimpleResponseDTO> listartoSelect(String search) {
        Specification<Student> filtros = Specification
                .where(StudentSpecification.search(search));

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
