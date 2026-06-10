package com.sistema.notas.respository.core;

import com.sistema.notas.entity.InstitutionalPerson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository

public interface PersonRepository extends JpaRepository<InstitutionalPerson, Integer> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Integer id);
    boolean existsByDui(String dui);
    boolean existsByDuiAndIdNot(String dui, Integer id);
}
