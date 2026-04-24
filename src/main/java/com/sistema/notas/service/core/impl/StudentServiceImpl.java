package com.sistema.notas.service.core.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.sistema.notas.dto.core.student.StudentFullResponseDTO;
import com.sistema.notas.dto.core.student.StudentRequestDTO;
import com.sistema.notas.dto.core.student.StudentResponseDTO;
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
    public StudentResponseDTO update(Integer id, StudentRequestDTO studentRequestDTO) {
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
                .where(CatalogoSpecification.<Student>searchContains(search))
                .and(CatalogoSpecification.<Student>createdBetween(startDate, endDate));

        Page<Student> students = studentRepository.findAll(filtros, pagable);

        return pageMapper.toPaginateResponse(
                students,
                studentMapper::toResponseDTO);
    }

    @Override
    public List<StudentSimpleResponseDTO> listartoSelect() {
        List<Student> students = studentRepository.findAll();

        return students.stream()
                .map(st -> new StudentSimpleResponseDTO(
                        st.getId(),
                        st.getfullName(),
                        st.getEmail(),
                        st.getRoutePhoto(),
                        st.getAge()))
                .toList();
    }

    @Override
    public StudentFullResponseDTO obtenerStudent(Integer id) {
        Student studentFind = studentRepository.findById(id).orElseThrow(
                () -> new BadRequestException("No se encontró el estudiante con id: " + id));

        return studentMapper.toFullResponse(studentFind);
    }

}
