package com.sistema.notas.service.catalogue;

import com.sistema.notas.entity.catalogues.Degree;

import java.util.List;

public interface DegreeService {
    Degree save(Degree degree);
    Degree update(Degree degree);
    Void delete(Integer id);

    List<Degree> findAll();
    Degree findById(Integer id);
    List<Degree> findByName(String name);
}
