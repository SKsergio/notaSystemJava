package com.sistema.notas.service.core.impl;

import java.time.LocalDate;
import java.util.List;

import com.sistema.notas.respository.core.PersonRepository;
import com.sistema.notas.specifications.core.people.PersonSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sistema.notas.dto.core.teacher.TeacherFullResponseDTO;
import com.sistema.notas.dto.core.teacher.TeacherRequestDTO;
import com.sistema.notas.dto.core.teacher.TeacherRequestUpdateDTO;
import com.sistema.notas.dto.core.teacher.TeacherResponseDTO;
import com.sistema.notas.dto.core.teacher.TeacherResponseEditDTO;
import com.sistema.notas.dto.core.teacher.TeacherSimpleResponseDTO;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.entity.core.Teacher;
import com.sistema.notas.exceptions.BadRequestException;
import com.sistema.notas.exceptions.ResourceNotFoundException;
import com.sistema.notas.mapper.PageMapper;
import com.sistema.notas.mapper.core.TeacherMapper;
import com.sistema.notas.respository.core.TeacherRepository;
import com.sistema.notas.service.core.TeacherService;
import com.sistema.notas.service.fileStorage.FileStorageService;
import com.sistema.notas.service.security.UserProvisioningService;
import com.sistema.notas.specifications.catalogue.CatalogoSpecification;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final TeacherRepository teacherRepository;
    private final PageMapper pageMapper;
    private final TeacherMapper teacherMapper;
    private final PersonRepository personRepository;
    private final UserProvisioningService userProvisioningService;
    
    //servicio de imagenes
    private final FileStorageService fileStorageService;

    // los trabajaremos despues
    @Override
    public void ChangeDegree(Integer newDegreeId) {
        throw new UnsupportedOperationException("Unimplemented method 'ChangeDegree'");
    }

    @Override
    public void RegisterInCourse(Integer courseId) {
        throw new UnsupportedOperationException("Unimplemented method 'RegisterInCourse'");
    }

    @Override
    public void EvaluateStudent(Integer studentId) {
        throw new UnsupportedOperationException("Unimplemented method 'EvaluateStudent'");
    }

    @Override
    @Transactional
    public TeacherResponseDTO save(TeacherRequestDTO teacher) {
        if (personRepository.existsByEmail(teacher.email())) {
            throw new BadRequestException("Ya hay una persona registrada en el sistema con el correo: " + teacher.email());
        }

        if (teacher.dui() != null && personRepository.existsByDui(teacher.dui())) {
            throw new BadRequestException("Ya hay una persona registrada con este DUI: " + teacher.dui());
        }

        Teacher entity = teacherMapper.toEntity(teacher);

        // aca ira el procesaminto de la foto, llamdno al servicio de imagnes
        if (teacher.photo() != null && !teacher.photo().isEmpty()) {
            String fileName = fileStorageService.storeFile(teacher.photo());
            entity.setRoutePhoto(fileName);
        }

        Teacher saved = teacherRepository.save(entity);
        userProvisioningService.provisionUserForCurrentTenant(saved.getEmail(), "TEACHER", saved.getId());
        return teacherMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public TeacherResponseDTO update(Integer id, TeacherRequestUpdateDTO teacher) {
        Teacher teacherFind = teacherRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No esixte ningun maestro con el id: " + id));

        if (personRepository.existsByEmailAndIdNot(teacher.email(), id)) {
            throw new BadRequestException("El correo " + teacher.email() + " ya está siendo usado por otro docente.");
        }

        if (personRepository.existsByDuiAndIdNot(teacher.dui(), id)) {
            throw new BadRequestException("El DUI " + teacher.dui() + " ya está siendo usado por otro docente.");
        }

        teacherMapper.updateEntityFromDTO(teacher, teacherFind);

        if (teacher.photo() != null && !teacher.photo().isEmpty()) {
            //obtener nombre de la antigua
            String oldNamePhoto = teacherFind.getRoutePhoto();

            //bueva foto
            String fileName = fileStorageService.storeFile(teacher.photo());
            teacherFind.setRoutePhoto(fileName);

            if (oldNamePhoto != null && !oldNamePhoto.isEmpty()) {
                fileStorageService.delteFile(oldNamePhoto);
            }
        }

        return teacherMapper.toResponseDTO(teacherFind);
    }

    @Override
    public void delete(Integer id) {
        Teacher teacherFind = teacherRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No esixte ningun maestro con el id: " + id));

        teacherRepository.delete(teacherFind);
        userProvisioningService.deactivateAccessForCurrentTenant(id, "TEACHER");
    }

    @Override
    public PaginateResponse<TeacherResponseDTO> obtenerTeacherPaginados(int page, int size, String search,
            LocalDate fromDate, LocalDate toDate) {
        Pageable pagable = PageRequest.of(page, size);

        Specification<Teacher> filtros = Specification
                .where(PersonSpecification.<Teacher>searchContains(search))
                .and(CatalogoSpecification.<Teacher>createdBetween(fromDate, toDate));

        Page<Teacher> teachers = teacherRepository.findAll(filtros, pagable);

        return pageMapper.toPaginateResponse(
                teachers,
                teacherMapper::toResponseDTO);
    }

    @Override
    public List<TeacherSimpleResponseDTO> listartoSelect() {
         List<Teacher> teachers = teacherRepository.findAll();

        return teachers.stream()
                .map(tch -> new TeacherSimpleResponseDTO(
                    tch.getId(),
                    tch.getfullName(), 
                    tch.getEmail(),
                    tch.getRoutePhoto(),
                    tch.getAge(),
                    tch.getDui()))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherFullResponseDTO obtenerTeachear(Integer id) {
        Teacher teacherFind = teacherRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No esixte ningun maestro con el id: " + id));

        return teacherMapper.toFullResponse(teacherFind);
    }

    @Override
    public TeacherResponseEditDTO obtenerTeacherEdit(Integer id) {
        Teacher teacherFind = teacherRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No esixte ningun maestro con el id: " + id));

        return teacherMapper.toResponseEditDTO(teacherFind);
    }

}
