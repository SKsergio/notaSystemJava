package com.sistema.notas.respository.core;

import com.sistema.notas.entity.InstitutionalPerson;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InstitutionalPersonRepository extends JpaRepository<InstitutionalPerson, Integer> {
    boolean existsByEmail(String email);
    boolean existsByEmailAndIdNot(String email, Integer id);
}
