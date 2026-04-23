package com.sistema.notas.entity.core;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import com.sistema.notas.entity.InstitutionalPerson;

import java.util.List;

@Entity
@Table(name = "teachers")
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@SQLDelete(sql = "UPDATE teachers SET active = false WHERE id = ?")
@SQLRestriction("active = true")
public class Teacher extends InstitutionalPerson{
    @Column(name ="speciality", length = 20, nullable = false)
    private String speciality;

    @Column(name ="dui", nullable = false, length = 10, unique = true)
    private String dui;

    @OneToMany(mappedBy = "tutor")
    private List<GradeDetail> assignedGrades;

    @OneToMany(mappedBy = "teacher")
    private List<Course> assignedCourses;
}
