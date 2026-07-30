package com.sistema.notas.entity.core;

import com.sistema.notas.entity.AuditableEntity;
import com.sistema.notas.entity.catalogues.Degree;
import com.sistema.notas.entity.catalogues.Section;
import com.sistema.notas.entity.enums.StatusEnum;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction; 


@Entity
@Table(name = "grade_details")
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE grade_details SET active = false WHERE id = ?")
@SQLRestriction("active = true")
public class GradeDetail extends AuditableEntity {

    @Column(name = "ability")
    private Integer ability;

    //startDate
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    //endDate
    @Column(name = "end_date", nullable = false)
    private LocalDate enDate;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusEnum status = StatusEnum.OPEN;

    @Transient
    public String getFullName() {
        if (this.degree == null || this.section == null || this.getYear() == 0) {
            return null;
        }
        
        return this.degree.getName() + " - " + this.section.getName() + " - " + this.startDate.getYear();
    }

    @Transient 
    public int getYear(){
        if (this.startDate == null) {
            return 0;
        }
        
        return this.startDate.getYear();
    }

    
}
