package com.sistema.notas.respository.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.sistema.notas.entity.core.Student;

public interface StudentRepository extends JpaRepository<Student, Integer>, JpaSpecificationExecutor<Student> {
    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Integer id);
}
