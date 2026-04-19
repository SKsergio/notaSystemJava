package com.sistema.notas.respository.catalogues;

import com.sistema.notas.entity.catalogues.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SectionRespository extends JpaRepository<Section, Integer>, JpaSpecificationExecutor<Section> {

    //metodos a implementarse
    Optional<Section> findByName(String name);
    boolean existsByCode(String name);
    Optional<Section> findById(Integer id);
}
