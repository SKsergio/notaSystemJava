package com.sistema.notas.entity.core;

import com.sistema.notas.entity.AuditableEntity;
import com.sistema.notas.entity.catalogues.Degree;
import com.sistema.notas.entity.catalogues.Section;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "grade_details", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"degree_id", "section_id", "year"})
})
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE grade_details SET active = false WHERE id = ?")
@SQLRestriction("active = true")
public class GradeDetail extends AuditableEntity {

    @Column(name = "ability")
    private Integer ability;

    @Column(name = "year")
    private Integer year;


    // Muchos Detalles pueden pertenecer a Un Grado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "degree_id", nullable = false)
    private Degree degree;

    // Muchos Detalles pueden tener Un mismo Tutor (Docente)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher tutor;

    // Muchos Detalles pertenecen a Una Sección (A, B, C...)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;
}
