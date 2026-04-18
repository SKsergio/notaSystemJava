package com.sistema.notas.respository.catalogues;

import com.sistema.notas.entity.catalogues.Degree;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface DegreeRespository extends JpaRepository<Degree,Integer>, JpaSpecificationExecutor<Degree> {
    Optional<Degree> findByName(String name);
    Page<Degree> findAll(Pageable pageable);
    boolean existsByCode(String code);

    Optional<Degree> findByCode(String code);
}
