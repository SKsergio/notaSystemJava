package com.sistema.notas.respository.catalogues;

import com.sistema.notas.entity.catalogues.Degree;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DegreeRespository extends JpaRepository<Degree,Integer> {
    @Override
    Optional<Degree> findById(Integer id);
    Optional<Degree> findByName(String name);
    boolean existsByCode(String name);
}
