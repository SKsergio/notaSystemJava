package com.sistema.notas.service.core.impl;

import com.sistema.notas.dto.core.managerStudents.AssignedStudentDetailDTO;
import com.sistema.notas.dto.core.managerStudents.ManagerStudentRequestDTO;
import com.sistema.notas.dto.core.managerStudents.ManagerStudentRequestEditDTO;
import com.sistema.notas.dto.core.managerStudents.ManagerStudentResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.entity.core.*;
import com.sistema.notas.exceptions.ResourceNotFoundException;
import com.sistema.notas.mapper.PageMapper;
import com.sistema.notas.mapper.core.ManagerStudentMapper;
import com.sistema.notas.mapper.core.StudentMapper;
import com.sistema.notas.respository.core.ManagerRepository;
import com.sistema.notas.respository.core.ManagerStudentRepository;
import com.sistema.notas.respository.core.StudentRepository;
import com.sistema.notas.service.core.ManagerStudentService;
import com.sistema.notas.specifications.catalogue.CatalogoSpecification;
import com.sistema.notas.specifications.core.ManagerStudent.ManagerStudentSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ManagerStudentServiceImpl implements ManagerStudentService {

    private final ManagerStudentRepository managerStudentRepository;
    private final ManagerStudentMapper managerStudentMapper;
    private final PageMapper pageMapper;

    //relaciones
    private final StudentRepository studentRepository;
    private final ManagerRepository managerRepository;

    @Override
    public ManagerStudentResponseDTO save(ManagerStudentRequestDTO requestDTO) {
        Student student = studentRepository.findById(requestDTO.studentId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "No existe ningun Alumno con el ID: " + requestDTO.studentId()));

        Manager manager = managerRepository.findById(requestDTO.managerId())
                .orElseThrow(
                        () -> new ResourceNotFoundException(
                                "No existe ningun encargado con el ID: " + requestDTO.managerId()));

        if (managerStudentRepository.isManagerStudentDuplicated(requestDTO.studentId(), requestDTO.managerId(), 0)) {
            throw new IllegalArgumentException("Ya existe una inscripción para este estudiante y curso");
        }

        ManagerStudents entity = managerStudentMapper.toEntity(requestDTO);
        entity.setStudent(student);
        entity.setManager(manager);
        ManagerStudents savedEntity = managerStudentRepository.save(entity);
        return managerStudentMapper.toResponseDTO(savedEntity);
    }

    @Override
    @Transactional
    public ManagerStudentResponseDTO update(ManagerStudentRequestEditDTO requestDTO, Integer id) {
        ManagerStudents registrationFind = managerStudentRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("No existe ningun registro con esta id"));

        managerStudentMapper.updateEntityFromDTO(requestDTO,registrationFind);
        ManagerStudents saveManagerStudent = managerStudentRepository.save(registrationFind);
        return managerStudentMapper.toResponseDTO(saveManagerStudent);
    }

    @Override
    public void delete(Integer id) {
        ManagerStudents registrationFind = managerStudentRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("No existe ningun registro con esta id"));

        managerStudentRepository.delete(registrationFind);
    }

    @Override
    public PaginateResponse<ManagerStudentResponseDTO> obtenerManagerStudents(int page, int size, String search, LocalDate fromDate, LocalDate toDate) {
        Pageable pagable = PageRequest.of(page, size);

        Specification<ManagerStudents> filtros = Specification
                .where(CatalogoSpecification.<ManagerStudents>createdBetween(fromDate, toDate));

        Page<ManagerStudents> managerStudents = managerStudentRepository.findAll(filtros, pagable);

        return pageMapper.toPaginateResponse(
                managerStudents,
                managerStudentMapper::toResponseDTO);
    }

    @Override
    public PaginateResponse<AssignedStudentDetailDTO> getAsignedStudents(int page, int size, String search, Integer managerId) {
        Pageable pageable = PageRequest.of(page, size);

        Specification<ManagerStudents> filtros = Specification
                .where(ManagerStudentSpecification.byManagerId(managerId))
                .and(ManagerStudentSpecification.searchStudentFields(search));

        Page<ManagerStudents> managerStudents = managerStudentRepository.findAll(filtros, pageable);

        return pageMapper.toPaginateResponse(
                managerStudents,
                managerStudentMapper::toAssignStudents
        );
    }

    @Override
    public List<ManagerStudentResponseDTO> listarToSelects() {
        List<ManagerStudents> registrations = managerStudentRepository.findAll();
        return managerStudentMapper.toResponseDTOList(registrations);
    }
}
