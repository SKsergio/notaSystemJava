package com.sistema.notas.config.seeders;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sistema.notas.entity.security.Permission;
import com.sistema.notas.entity.security.Role;
import com.sistema.notas.respository.security.PermissionRepository;
import com.sistema.notas.respository.security.RoleRepository;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class PermissionAndRoleSeeder {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public void seed() {
        if (roleRepository.count() == 0) {
            System.out.println("🛡️ [SEEDER] Permisos y Roles base...");

            // 1. Crear Permisos (Agrega aquí todos los que necesites en el futuro)
            Permission studentRead = createPermission("student:read", "Ver Estudiantes", "ESTUDIANTES");
            Permission studentWrite = createPermission("student:write", "Modificar Estudiantes", "ESTUDIANTES");
            Permission studentDelete = createPermission("student:delete", "Eliminar Estudiantes", "ESTUDIANTES");
            Permission restoreDelete = createPermission("student:restore", "Restaurar Estudiantes", "ESTUDIANTES");
            Permission gradeRead = createPermission("grade:read", "Ver Notas", "NOTAS");
            Permission gradeWrite = createPermission("grade:write", "Modificar Notas", "NOTAS");
            Permission managerRead = createPermission("manager:read", "Ver Estudiantes a Cargo", "ENCARGADOS");

            // 2. Crear Roles y asignarles sus permisos
            Role admin = new Role();
            admin.setName("ADMIN");
            admin.setDescription("Administrador Global del Colegio");
            admin.getPermissions().addAll(Set.of(studentRead, studentWrite, studentDelete, restoreDelete, gradeRead, gradeWrite));
            roleRepository.save(admin);

            Role teacher = new Role();
            teacher.setName("TEACHER");
            teacher.setDescription("Profesor Titular");
            teacher.getPermissions().addAll(Set.of(studentRead, gradeRead, gradeWrite)); // El profesor no edita alumnos
            roleRepository.save(teacher);

            Role student = new Role();
            student.setName("STUDENT");
            student.setDescription("Estudiante Matriculado");
            student.getPermissions().addAll(Set.of(gradeRead)); // El alumno solo ve notas
            roleRepository.save(student);

            Role manager = new Role();
            manager.setName("MANAGER");
            manager.setDescription("Encargado / Tutor de Estudiantes");
            manager.getPermissions().addAll(Set.of(managerRead, gradeRead)); // Solo lectura
            roleRepository.save(manager);

            System.out.println("✅ [SEEDER] Permisos y Roles creados exitosamente.");
        }
    }

    private Permission createPermission(String code, String name, String module) {
        return permissionRepository.findByCode(code).orElseGet(() -> {
            Permission p = new Permission();
            p.setCode(code);
            p.setName(name);
            p.setModule(module);
            return permissionRepository.save(p);
        });
    }
}