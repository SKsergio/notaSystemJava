package com.sistema.notas.respository.catalogues;

import com.sistema.notas.entity.catalogues.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Integer>, JpaSpecificationExecutor<Subject> {
    Optional<Subject> findByName(String name);
    boolean existsByCode(String name);
    Optional<Subject> findById(Integer id);
}
