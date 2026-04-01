package com.sistema.notas.service.catalogue;

import com.sistema.notas.entity.catalogues.Subject;

import java.util.List;

public interface SubjectService {
    Subject save(Subject subject);
    Subject update(Subject subject);
    Void delete(Integer id);

    List<Subject> findAll();
    Subject findById(Integer id);
    Subject findByName(String name);
}
