package com.sistema.notas.config.security;

import com.sistema.notas.entity.enums.Role;
import com.sistema.notas.entity.security.User;
import com.sistema.notas.entity.core.Student;
import com.sistema.notas.entity.core.Teacher;
import com.sistema.notas.respository.core.StudentRepository;
import com.sistema.notas.respository.core.TeacherRepository;
import com.sistema.notas.respository.security.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminBootstrap implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        createAdmin();
        createTeachers();
        createStudents();

        System.out.println("USUARIOS INICIALES VERIFICADOS");
    }

    private void createAdmin() {

        boolean exists = userRepository
                .findByEmail("admin@notas.local")
                .isPresent();

        if (exists) {
            return;
        }

        User admin = new User();

        admin.setEmail("admin@notas.local");

        admin.setPasswordHash(
                passwordEncoder.encode("Admin123!")
        );

        admin.setRole(Role.ADMIN);

        userRepository.save(admin);

        System.out.println("ADMIN CREADO");
    }

    private void createTeachers() {

        for (Teacher teacher : teacherRepository.findAll()) {

            boolean exists = userRepository
                    .findByEmail(teacher.getEmail())
                    .isPresent();

            if (exists) {
                continue;
            }

            User user = new User();

            user.setEmail(teacher.getEmail());

            user.setPasswordHash(
                    passwordEncoder.encode("123456")
            );

            user.setRole(Role.TEACHER);

            user.setTeacherId(teacher.getId());

            userRepository.save(user);

            System.out.println("TEACHER USER CREADO: " + teacher.getEmail());
        }
    }

    private void createStudents() {

        for (Student student : studentRepository.findAll()) {

            boolean exists = userRepository
                    .findByEmail(student.getEmail())
                    .isPresent();

            if (exists) {
                continue;
            }

            User user = new User();

            user.setEmail(student.getEmail());

            user.setPasswordHash(
                    passwordEncoder.encode("123456")
            );

            user.setRole(Role.STUDENT);

            user.setStudentId(student.getId());

            userRepository.save(user);

            System.out.println("STUDENT USER CREADO: " + student.getEmail());
        }
    }
}