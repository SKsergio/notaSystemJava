package com.sistema.notas.service.catalogue;

import com.sistema.notas.entity.catalogues.Section;

import java.util.List;
import java.util.Optional;

public interface SectionService {
    //Crear
    Section save(Section section);
    //Listar
    List<Section> listSections();

    //buscadores
    Optional<Section> findByName(String name);
    Optional<Section> findById(Integer id);

    //actualizar
    Section update(Section section);
    //eliminar
    Void delete(Integer id);
}
