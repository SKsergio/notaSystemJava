package com.sistema.notas.service.catalogue;

import com.sistema.notas.entity.catalogues.Degree;

import java.util.List;
import java.util.Optional;

public interface DegreeService {
    Degree save(Degree degree);
    Degree update(Integer id, Degree degree);
    void delete(Integer id);

    List<Degree> findAll();
    Optional<Degree> findById(Integer id);
    Optional<Degree> findByName(String name);
    Optional<Degree> findByCode(String code);
}
