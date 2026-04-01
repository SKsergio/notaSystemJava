package com.sistema.notas.respository.catalogues;

import com.sistema.notas.entity.catalogues.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SectionRespository extends JpaRepository<Section, Integer> {

    //metodos a implementarse
    Optional<Section> findByName(String name);
    Optional<Section> findById(Integer id);
    boolean existsByCode(String name);
}
