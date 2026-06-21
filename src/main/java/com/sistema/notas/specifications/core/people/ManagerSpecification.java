package com.sistema.notas.specifications.core.people;

import org.springframework.data.jpa.domain.Specification;

public class ManagerSpecification {
    public static <Manager> Specification<Manager> searchContains(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) {
                return cb.conjunction();
            }

            String value = "%" + search.toUpperCase() + "%";

            return cb.or(
                    cb.like(cb.upper(root.get("firstName")), value),
                    cb.like(cb.upper(root.get("secondName")), value),
                    cb.like(cb.upper(root.get("firstLastName")), value),
                    cb.like(cb.upper(root.get("secondLastName")), value),
                    cb.like(cb.upper(root.get("email")), value),
                    cb.like(cb.upper(root.get("phoneNumber")), value));
        };
    }
}
