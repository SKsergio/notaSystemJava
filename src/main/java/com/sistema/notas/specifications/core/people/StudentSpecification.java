package com.sistema.notas.specifications.core.people;

import com.sistema.notas.entity.core.Student;
import org.springframework.data.jpa.domain.Specification;

public class StudentSpecification {

    public static Specification<Student> carnetContains(String carnet) {
        return (root, query, cb) -> {
            if (carnet == null || carnet.isBlank()){
                return cb.conjunction();
            }
            String value = "%" + carnet.toUpperCase() +"%";
            return cb.like(cb.upper(root.get("carnet")), value);
        };
    }

    public static Specification<Student> searchAll(String search) {
        // Unimos dinámicamente la búsqueda global (Persona) con la específica (Estudiante)
        return Specification
                .where(PersonSpecification.<Student>searchContains(search))
                .or(carnetContains(search));
    }
}