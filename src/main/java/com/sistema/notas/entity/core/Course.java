package com.sistema.notas.entity.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.sistema.notas.entity.AbstractCatalogo;
import com.sistema.notas.entity.AuditableEntity;
import com.sistema.notas.entity.catalogues.Period;
import com.sistema.notas.entity.catalogues.Subject;


@Entity
@Table(name = "courses", 
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"grade_detail_id", "subject_id", "period_id"})
    }
)
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE courses SET active = false WHERE id = ?")
@SQLRestriction("active = true")
public class Course extends AbstractCatalogo{
    @Column(name = "valoraty_unity")
    private double valorityUnity;

    //no se enviara del fronted
    @Column(name = "status", nullable = false)
    private Integer status;

    //muchos cursos pertenecen a un grado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_detail_id", nullable = false)
    private GradeDetail gradeDetail;

    //muchos cursos pertenecen a una misma materia
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    //muchos Cursos pueden ser impartidos por un profesor
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    //muchas materias pertenecen a un curso
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "period_id", nullable = false)
    private Period period;
}


