package com.sistema.notas.entity.core;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.sistema.notas.entity.InstitutionalPerson;

import java.util.List;

@Entity
@Table(name = "teachers")
@Getter @Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "id")
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
