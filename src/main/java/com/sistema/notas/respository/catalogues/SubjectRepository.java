package com.sistema.notas.respository.catalogues;

import com.sistema.notas.entity.catalogues.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    Optional<Subject> findByName(String name);
    boolean existsByCode(String name);
    Optional<Subject> findById(Integer id);
}
