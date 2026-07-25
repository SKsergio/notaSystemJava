package com.sistema.notas.specifications.config.tenant;

import org.springframework.data.jpa.domain.Specification;
import com.sistema.notas.entity.Tenant;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class TenantSpecification {
   public static Specification<Tenant> search(String search) {
        return (Root<Tenant> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            if (search == null || search.trim().isEmpty()) {
                return cb.conjunction();
            }

            String searchPattern = "%" + search.toLowerCase() + "%";

            return cb.or(
                    cb.like(cb.lower(root.get("legalName")), searchPattern),
                    cb.like(cb.lower(root.get("nit")), searchPattern),
                    cb.like(cb.lower(root.get("nrc")), searchPattern),
                    cb.like(cb.lower(root.get("name")), searchPattern)
            );
        };
    } 
}
