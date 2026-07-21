package com.sistema.notas.specifications.core.ManagerStudent;

import com.sistema.notas.entity.core.ManagerStudents;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class ManagerStudentSpecification {

    //filtrando por encargado
    public static Specification<ManagerStudents> byManagerId(Integer managerId){
        return (root, query, cb) -> {
            if (Long.class != query.getResultType()){
                root.fetch("student", JoinType.LEFT);
            }
            return cb.equal(root.get("manager").get("id"), managerId);
        };
    }

    public static Specification<ManagerStudents> searchStudentFields(String search){
        return (root, query, cb) ->{
            if (search == null || search.isBlank()){
                return cb.conjunction();
            }

            String value = "%" + search.toUpperCase() + "%";
            Join<Object, Object> student = root.join("student");

            return cb.or(
                cb.like(cb.upper(student.get("firstName")), value),
                cb.like(cb.upper(student.get("firstLastName")), value),
                cb.like(cb.upper(student.get("carnet")), value)
            );
        };
    }
}
