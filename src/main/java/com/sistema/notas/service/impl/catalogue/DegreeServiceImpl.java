package com.sistema.notas.service.impl.catalogue;

import com.sistema.notas.entity.catalogues.Degree;
import com.sistema.notas.respository.catalogues.DegreeRespository;
import com.sistema.notas.service.catalogue.DegreeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DegreeServiceImpl implements DegreeService {

    //inyectamos al repository
    private final DegreeRespository degreeRespository;

    @Override
    public Degree save(Degree degree) {
//        if(degreeRespository.existsByCode(degree.getCode())){
//
//        }
        return null;
    }

    @Override
    public Degree update(Degree degree) {
        return null;
    }

    @Override
    public Void delete(Integer id) {
        return null;
    }

    @Override
    public List<Degree> findAll() {
        return List.of();
    }

    @Override
    public Degree findById(Integer id) {
        return null;
    }

    @Override
    public List<Degree> findByName(String name) {
        return List.of();
    }
}
