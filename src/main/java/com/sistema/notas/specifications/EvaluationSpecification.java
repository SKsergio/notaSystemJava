package com.sistema.notas.specifications;

import com.sistema.notas.entity.core.Evaluation;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class EvaluationSpecification {

    public static Specification<Evaluation> search(String keyword) {
        return (Root<Evaluation> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String searchPattern = "%" + keyword.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("course").get("name")), searchPattern)
            );
        };
    }
}