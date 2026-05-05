package com.sistema.notas.specifications;

import com.sistema.notas.entity.core.CourseRegistration;
import com.sistema.notas.entity.enums.EnrollmentStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class CourseRegistrationSpecification {

    public static Specification<CourseRegistration> search(String keyword) {
        return (Root<CourseRegistration> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (keyword == null || keyword.trim().isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            String searchPattern = "%" + keyword.toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("student").get("fullName")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("student").get("firstName")), searchPattern),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("course").get("name")), searchPattern)
            );
        };
    }

    public static Specification<CourseRegistration> hasStatus(EnrollmentStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<CourseRegistration> byCourse(Long courseId) {
        return (root, query, criteriaBuilder) -> {
            if (courseId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("course").get("id"), courseId);
        };
    }

    public static Specification<CourseRegistration> betweenDates(LocalDateTime fromDate, LocalDateTime toDate) {
        return (root, query, criteriaBuilder) -> {
            if (fromDate == null && toDate == null) {
                return criteriaBuilder.conjunction();
            }

            if (fromDate != null && toDate != null) {
                return criteriaBuilder.between(root.get("createdAt"), fromDate, toDate);
            } else if (fromDate != null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), fromDate);
            } else {
                return criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), toDate);
            }
        };
    }
}