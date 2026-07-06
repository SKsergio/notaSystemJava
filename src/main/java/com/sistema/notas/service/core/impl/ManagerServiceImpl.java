package com.sistema.notas.service.core.impl;

import com.sistema.notas.dto.core.managers.*;
import com.sistema.notas.dto.generics.PaginateResponse;
import com.sistema.notas.entity.InstitutionalPerson;
import com.sistema.notas.entity.core.GradeDetail;
import com.sistema.notas.entity.core.Manager;
import com.sistema.notas.entity.core.Student;
import com.sistema.notas.entity.core.Teacher;
import com.sistema.notas.entity.enums.EnrollmentStatus;
import com.sistema.notas.mapper.core.StudentMapper;
import com.sistema.notas.specifications.catalogue.CatalogoSpecification;
import com.sistema.notas.specifications.core.people.PersonSpecification;
import com.sistema.notas.specifications.core.people.StudentSpecification;
import com.sistema.notas.specifications.core.people.TeacherSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.sistema.notas.entity.enums.Role;
import com.sistema.notas.entity.security.User;
import com.sistema.notas.exceptions.BadRequestException;
import com.sistema.notas.mapper.PageMapper;
import com.sistema.notas.mapper.core.ManagerMapper;
import com.sistema.notas.respository.core.ManagerRepository;
import com.sistema.notas.respository.core.PersonRepository;
import com.sistema.notas.respository.security.UserRepository;
import com.sistema.notas.service.core.ManagerService;
import com.sistema.notas.service.fileStorage.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.data.domain.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {
    //INYECCION DE DEPENDENCIAS
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final ManagerRepository managerRepository;
    private final PageMapper pageMapper;
    private final ManagerMapper managerMapper;
    private final PersonRepository personRepository;
    //servicio de imagnes
    private final FileStorageService fileStorageService;
    private final StudentMapper studentMapper;


    @Override
    @Transactional
    public ManagerResponseDTO save(ManagerRequestDTO manager) {
        if (personRepository.existsByEmail(manager.email())){
            throw new BadRequestException("Ya hay una persona registrada en el sistema con el correo: " + manager.email());
        }

        if (manager.dui() != null && personRepository.existsByDui(manager.dui())) {
            throw new BadRequestException("Ya hay una persona registrada con este DUI: " + manager.dui());
        }

        Manager entity = managerMapper.toEntity(manager);

        if (manager.photo() != null && !manager.photo().isEmpty()) {
            String fileName = fileStorageService.storeFile(manager.photo());
            entity.setRoutePhoto(fileName);
        }

        Manager saved = managerRepository.save(entity);
        if (!userRepository.existsByEmail(saved.getEmail())) {

            User user = new User();
            user.setEmail(saved.getEmail());
            user.setPasswordHash(
                    passwordEncoder.encode("123456")
            );
            user.setRole(Role.TEACHER);//luego se guardara como encargadop
            user.setTeacherId(saved.getId());
            user.setFirstLogin(true);
            userRepository.save(user);
        }
        return managerMapper.toResponseDTO(saved);
    }

    @Override
    @Transactional
    public ManagerResponseDTO update(Integer id, ManagerRequestUpdateDTO manager) {
        Manager managerFind = managerRepository.findById(id).orElseThrow(
                ()->new BadRequestException("No ecxiste ningun encargado con el id: " + id)
        );

        //validando correos  y dui en la tabla principal
        if (personRepository.existsByEmailAndIdNot(manager.email(), id)) {
            throw new BadRequestException("El correo " + manager.email() + " ya está siendo usado por otro encargado.");
        }

        if (personRepository.existsByDuiAndIdNot(manager.dui(), id)) {
            throw new BadRequestException("El DUI " + manager.dui() + " ya está siendo usado por otro encargado.");
        }

        managerMapper.updateEntityFromDTO(manager, managerFind);

        if (manager.photo() != null && !manager.photo().isEmpty()){
            String oldNamePhoto = managerFind.getRoutePhoto();

            String fileName = fileStorageService.storeFile(manager.photo());
            managerFind.setRoutePhoto(fileName);

            if (oldNamePhoto != null && oldNamePhoto.isEmpty()){
                fileStorageService.delteFile(oldNamePhoto);
            }
        }

        return managerMapper.toResponseDTO(managerFind);

    }

    @Override
    public void delete(Integer id) {
        Manager managerFind = managerRepository.findById(id).orElseThrow(
                ()->new BadRequestException("No ecxiste ningun encargado con el id: " + id)
        );

        managerRepository.delete(managerFind);
    }

    @Override
    public PaginateResponse<ManagerResponseDTO> obtenerManagerPaginados(int page, int size, String search, LocalDate fromDate, LocalDate toDate) {
        Pageable pagable = PageRequest.of(page, size);

        Specification<Manager> filtros = Specification
                .where(PersonSpecification.<Manager>searchContains(search))
                .and(CatalogoSpecification.<Manager>createdBetween(fromDate, toDate));

        Page<Manager> managers = managerRepository.findAll(filtros, pagable);

        return pageMapper.toPaginateResponse(
                managers,
                managerMapper::toResponseDTO);
    }

    @Override
    public List<ManagerSimpleResponseDTO> listartoSelect(String search) {
        Specification<Manager> filtros = Specification
                .where(PersonSpecification.searchContains(search));

        List<Manager>  managers= managerRepository.findAll(filtros);

        return managers.stream().
                map(managerMapper::toSimpleResponseDTO).
                toList();
    }

    @Override
    public ManagerFullResponseDTO obtenerManager(Integer id) {
        Manager managerFind = managerRepository.findManagerWithStudentsById(id).orElseThrow(
                () -> new BadRequestException("No se encontró el encargado con id: " + id));

        return managerMapper.toFullResponse(managerFind);

    }

    @Override
    public ManagerResponseEditDTO obtenerManagertoEdit(Integer id) {
        Manager managerFind = managerRepository.findById(id).orElseThrow(
                () -> new BadRequestException("No se encontró el encargado con id: " + id));

        return managerMapper.toResponseEditDTO(managerFind);
    }
}
