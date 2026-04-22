package com.sistema.notas.respository.core;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.sistema.notas.entity.core.Course;

public interface CoursesRespository extends JpaRepository<Course, Integer>, JpaSpecificationExecutor<Course>{
    
    @Query("SELECT COUNT(c) > 0 FROM Course c WHERE c.subject.id = :subjectId AND c.gradeDetail.id = :gradeDetailId AND c.period.id = :periodId")
    boolean isCourseDuplicated(
            @Param("subjectId") Integer subcjectId, 
            @Param("gradeDetailId") Integer gradeDetailId, 
            @Param("periodId") Integer periodId
    );

    @Query("SELECT COUNT(c) > 0 FROM Course c WHERE c.subject.id = :subjectId AND c.gradeDetail.id = :gradeDetailId AND c.period.id = :periodId AND c.id != :courseId")
    boolean isCourseDuplicatedForUpdate(
            @Param("subjectId") Integer subcjectId, 
            @Param("gradeDetailId") Integer gradeDetailId, 
            @Param("periodId") Integer periodId,
            @Param("courseId") Integer courseId
    );
}
